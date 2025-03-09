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
        private readonly IRabbitService _rabbitService;

        public HomeController(IRabbitService rabbitService)
        {
            _rabbitService = rabbitService;
        }
        
        // GET: HomeController
        [Route("/")]
        public async Task<ActionResult> Index()
        {

            return new JsonResult(new
            {
                igat = "matthew"
            });
        }

    }
}
