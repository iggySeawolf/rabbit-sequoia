using RabbitServices;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();

builder.Configuration.AddJsonFile("appsettings.json", optional: false, reloadOnChange: true)
                     .AddJsonFile($"appsettings.{builder.Environment.EnvironmentName}.json", optional: true, reloadOnChange: true);

builder.Services.AddSingleton<IJobPostService, JobPostService>();
builder.Services.AddHostedService<RabbitConsumerService>();

var app = builder.Build();

if(!app.Environment.IsProduction())
{
    app.UseDeveloperExceptionPage();
}

//app.UseStaticFiles();
app.UseRouting();
app.MapControllers();

app.Run();
