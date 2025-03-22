using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Text;

using System.Net;

using System.Diagnostics;
using RabbitServices;
using dotnet_section.Services;
using Microsoft.AspNetCore.Authorization;


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
        [Route("")]
        public async Task<ActionResult> Index() //Views/Home/Index.cshtml
        {
            return View();

        }
        [Route("/secure")]
        [Authorize]
        public async Task<ActionResult> Secure()
        {
            return Ok("<h1>Bisain</h1>");

        }

        [Route("/worksecure")]
        [Authorize]
        public async Task<ActionResult> WorkSecure()
        {
            var abc = _jobAggregatorService.RefreshJobPosts();
            return new JsonResult(abc);

        }
        [Route("/work")]
        [Authorize]
        public async Task<ActionResult> Work()
        {
            var abc = _jobAggregatorService.RefreshJobPosts();
            return new JsonResult(abc);

        }

    }
}
