using System.Collections.Concurrent;
using System.Data.Common;
using System.Diagnostics;
using System.Text;
using System.Text.Json;
using Microsoft.Extensions.Hosting;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using RabbitServices.Models;

namespace RabbitServices
{
    public class RabbitConsumerService : BackgroundService
    {
        private string QUEUE_NAME = "iggy_demo";
        private readonly IJobPostService _jobPostService;

        private readonly ConnectionFactory _factory;
        private IConnection _connection;
        private IChannel _channel;

        public RabbitConsumerService(IJobPostService jps)
        {
            //var factory = new ConnectionFactory { HostName = "rabbit-sequoia-management" };
            // Dependency injection first
            _jobPostService = jps;

            _factory = new ConnectionFactory { HostName = "localhost" };

        }

        private async Task InitRabbitMqAsync()
        {
            _connection = await _factory.CreateConnectionAsync();
            _channel = await _connection.CreateChannelAsync();

            await _channel.QueueDeclareAsync(queue: QUEUE_NAME, durable: true, exclusive: false, autoDelete: false,
                arguments: null);
        }

        protected override async Task ExecuteAsync(CancellationToken stoppingToken)
        {
            await InitRabbitMqAsync();

            Debug.Print(" [*] Waiting for messages.");

            var consumer = new AsyncEventingBasicConsumer(_channel);
            consumer.ReceivedAsync += (model, ea) =>
            {
                var body = ea.Body.ToArray();
                var messageJSON = Encoding.UTF8.GetString(body);

                Debug.Print($" [x] Received {messageJSON}");
                try
                {
                    var jobPost = JsonSerializer.Deserialize<JobPost>(messageJSON, new JsonSerializerOptions
                    {
                        PropertyNameCaseInsensitive = true
                    });
                    if(jobPost != null)
                    {
                        _jobPostService.insertJobPost(jobPost);
                    }

                } catch (Exception ex)
                {

                }
                return Task.CompletedTask;
            };

            await _channel.BasicConsumeAsync(QUEUE_NAME, autoAck: true, consumer: consumer);

            Debug.Print(" Press [enter] to exit.");
        }

        public override Task StopAsync(CancellationToken cancellationToken)
        {
            _channel?.CloseAsync();
            _connection?.CloseAsync();
            return Task.CompletedTask;
        }
    }
}
