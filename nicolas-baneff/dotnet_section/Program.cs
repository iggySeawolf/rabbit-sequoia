using System.Text;
using dotnet_section.Controllers;
using dotnet_section.Services;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.Extensions.Options;
using Microsoft.IdentityModel.Tokens;
using RabbitServices;

var builder = WebApplication.CreateBuilder(args);
int TopNParameter = 5;
builder.Services.AddControllersWithViews();

builder.Configuration.AddJsonFile("appsettings.json", optional: false, reloadOnChange: true)
                     .AddJsonFile($"appsettings.{builder.Environment.EnvironmentName}.json", optional: true, reloadOnChange: true);

builder.Services.AddSingleton<IJobPostService, JobPostService>();
builder.Services.AddSingleton<IJobPostAggregator, JobPostAggregator>(sp =>
{
    var jobPostService = sp.GetRequiredService<IJobPostService>();
    return new JobPostAggregator(jobPostService, TopNParameter);
});

builder.Services.AddHostedService<RabbitConsumerService>();


// JWT Configuration
var key = "your_secret_key_here"; // Replace with a strong key
var keyBytes = Encoding.UTF8.GetBytes(key);

builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
    .AddJwtBearer(options =>
    {
        options.TokenValidationParameters = new TokenValidationParameters
        {
            ValidateIssuer = false,
            ValidateAudience = false,
            ValidateLifetime = true,
            ValidateIssuerSigningKey = true,
            IssuerSigningKey = new SymmetricSecurityKey(keyBytes)
        };
    });

builder.Services.AddAuthorization();

var app = builder.Build();

if(!app.Environment.IsProduction())
{
    app.UseDeveloperExceptionPage();
}

//app.UseStaticFiles();
app.UseRouting();
app.MapControllers();

//jwt
app.UseAuthentication();
app.UseAuthorization();

app.Run();
