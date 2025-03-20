using dotnet_section.Controllers;
using dotnet_section.Services;
using Microsoft.Extensions.Options;
using RabbitServices;

var builder = WebApplication.CreateBuilder(args);
int TopNParameter = 5;
builder.Services.AddControllers();

builder.Configuration.AddJsonFile("appsettings.json", optional: false, reloadOnChange: true)
                     .AddJsonFile($"appsettings.{builder.Environment.EnvironmentName}.json", optional: true, reloadOnChange: true);

builder.Services.AddSingleton<IJobPostService, JobPostService>();
builder.Services.AddSingleton<IJobPostAggregator, JobPostAggregator>(sp =>
{
    var jobPostService = sp.GetRequiredService<IJobPostService>();
    return new JobPostAggregator(jobPostService, TopNParameter);
});

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
