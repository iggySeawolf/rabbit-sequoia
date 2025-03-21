using System.Collections.Concurrent;
using System.Data.Common;
using System.Diagnostics;
using System.Text;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using RabbitServices.Models;

namespace RabbitServices
{
    public class JobPostService : IJobPostService   
    {
        private const int QUEUE_SIZE = 10;

        public ConcurrentQueue<JobPost> JobPosts { get; } = new ConcurrentQueue<JobPost>();

        public JobPostService()
        {
        }

        public IEnumerable<JobPost> GetAllJobsFromQueue()
        {
            return JobPosts.ToList();
        }
        public void insertJobPost(JobPost jp)
        {
            JobPosts.Enqueue(jp);

            if(JobPosts.Count > QUEUE_SIZE)
            {
                // do some leetcode stuff, for each query parameter, return top 5 skills tags
                // then clear the queue
                ProcessJobPost();
                while (JobPosts.TryDequeue(out _)) { }
            }

        }

        public void ProcessJobPost()
        {

        }

    }   
}
