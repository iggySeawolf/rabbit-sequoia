using RabbitServices.Models;

namespace dotnet_section.Services
{
    public interface IJobPostAggregator
    {
        public void InsertJobPost(JobPost jp);
        public Dictionary<string, List<string>> RefreshJobPosts();
    }
}
