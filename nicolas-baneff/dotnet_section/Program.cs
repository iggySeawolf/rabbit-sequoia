using RabbitServices;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddSingleton<IRabbitService, RabbitService>();
var app = builder.Build();

//app.MapGet("/", () => "Igat Hello World!");

//app.UseStaticFiles();
app.UseRouting();
app.MapControllers();

app.Run();
