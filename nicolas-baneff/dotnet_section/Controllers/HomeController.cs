using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Text;

using System.Net;

using System.Diagnostics;
using RabbitServices;
using dotnet_section.Services;


namespace dotnet_section.Controllers
{
    public class HomeController : Controller
    {
        private readonly IWebHostEnvironment _webHostEnvironment;
        private readonly IJobPostAggregator _jobAggregatorService;

        public HomeController(IJobPostAggregator jobAggregatorService, IWebHostEnvironment webHostEnvironment)
        {
            _jobAggregatorService = jobAggregatorService;
            _webHostEnvironment = webHostEnvironment;

            Debug.Print("HomeController.cs running on environment "+webHostEnvironment.EnvironmentName);
            Debug.Print("HomeController.cs content rootpath is: "+webHostEnvironment.ContentRootPath);
        }

        // GET: HomeController
        [Route("/")]
        public async Task<ActionResult> Index()
        {
            var abc=  _jobAggregatorService.RefreshJobPosts();
            return new JsonResult(abc);

        }
    }
}
