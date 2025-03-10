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
        private readonly IJobPostService _rabbitService;

        public HomeController(IJobPostService rabbitService)
        {
            _rabbitService = rabbitService;
        }

        // GET: HomeController
        [Route("/")]
        public async Task<ActionResult> Index()
        {

            return new JsonResult(_rabbitService.GetAllJobsFromQueue());

        }
    }
}
