using RabbitServices;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();

builder.Services.AddSingleton<IJobPostService, JobPostService>();
builder.Services.AddHostedService<RabbitConsumerService>();

var app = builder.Build();

//app.MapGet("/", () => "Igat Hello World!");

//app.UseStaticFiles();
app.UseRouting();
app.MapControllers();

app.Run();
