using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using System.Text;

using System.Net;

using System.Diagnostics;
using RabbitServices;


namespace dotnet_section.Controllers
{
    public class HomeController : Controller
    {
        private readonly IWebHostEnvironment _webHostEnvironment;
        private readonly IJobPostService _rabbitService;

        public HomeController(IJobPostService rabbitService, IWebHostEnvironment webHostEnvironment)
        {
            _rabbitService = rabbitService;
            _webHostEnvironment = webHostEnvironment;

            Debug.Print("HomeController.cs running on environment "+webHostEnvironment.EnvironmentName);
            Debug.Print("HomeController.cs content rootpath is: "+webHostEnvironment.ContentRootPath);
        }

        // GET: HomeController
        [Route("/")]
        public async Task<ActionResult> Index()
        {
            return new JsonResult(_rabbitService.GetAllJobsFromQueue());

        }
    }
}
