using System.Diagnostics;
using System.Text;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;

namespace RabbitServices
{
    public class RabbitService : IRabbitService
    {
        private string QUEUE_NAME = "iggy_demo";

        public RabbitService()
        {

        }

        public async void Receive()
        {
            //var factory = new ConnectionFactory { HostName = "rabbit-sequoia-management" };
            var factory = new ConnectionFactory { HostName = "localhost" };
            using var connection = await factory.CreateConnectionAsync();
            using var channel = await connection.CreateChannelAsync();

            await channel.QueueDeclareAsync(queue: QUEUE_NAME, durable: true, exclusive: false, autoDelete: false,
                arguments: null);

            Debug.Print(" [*] Waiting for messages.");

            var consumer = new AsyncEventingBasicConsumer(channel);
            consumer.ReceivedAsync += (model, ea) =>
            {
                var body = ea.Body.ToArray();
                var message = Encoding.UTF8.GetString(body);
                Debug.Print($" [x] Received {message}");
                return Task.CompletedTask;
            };

            await channel.BasicConsumeAsync(QUEUE_NAME, autoAck: true, consumer: consumer);

            Debug.Print(" Press [enter] to exit.");
        }
    }
}
