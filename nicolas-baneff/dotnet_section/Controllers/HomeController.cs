using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Text;

using System.Net;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;


namespace dotnet_section.Controllers
{
    public class HomeController : Controller
    {
        private const string QUEUE_NAME = "iggy_demo";

        // GET: HomeController
        [Route("/")]
        public async Task<ActionResult> Index()
        {
            //var factory = new ConnectionFactory { HostName = "rabbit-sequoia-management" };
            var factory = new ConnectionFactory { HostName = "localhost" };
            using var connection = await factory.CreateConnectionAsync();
            using var channel = await connection.CreateChannelAsync();

            await channel.QueueDeclareAsync(queue: QUEUE_NAME, durable: true, exclusive: false, autoDelete: false,
                arguments: null);

            Console.WriteLine(" [*] Waiting for messages.");

            var consumer = new AsyncEventingBasicConsumer(channel);
            consumer.ReceivedAsync += (model, ea) =>
            {
                var body = ea.Body.ToArray();
                var message = Encoding.UTF8.GetString(body);
                Console.WriteLine($" [x] Received {message}");
                return Task.CompletedTask;
            };

            await channel.BasicConsumeAsync(QUEUE_NAME, autoAck: true, consumer: consumer);

            Console.WriteLine(" Press [enter] to exit.");
            Console.ReadLine();

            return new JsonResult(new
            {
                igat = "matthew"
            });
        }

    }
}
