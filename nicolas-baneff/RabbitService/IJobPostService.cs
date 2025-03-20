using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RabbitServices.Models;

namespace RabbitServices
{
    public interface IJobPostService
    {
        public IEnumerable<JobPost> GetAllJobsFromQueue();
        public void insertJobPost(JobPost jp);
        public void ProcessJobPost();
    }

}
