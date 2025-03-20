using dotnet_section.Services;
using RabbitServices;
using RabbitServices.Models;

namespace dotnet_section.Services
{
    //{"queryParameter":"resume:.NET","companyName":"Notion","title":"Organic Content Lead","skillsTags":["Automation","B2B","Cross-functional Team Leadership","Content Marketing","Artificial Intelligence","Content Development","SEO","Software as a Service","Performance Analysis"],"jobPostedWhen":"2025-03-19T15:30:11.000Z","messagePublishedOn":"2025-03-20T04:08:00.580Z"}
    public class JobPostAggregator : IJobPostAggregator
    {

        private JobPostCollection jpc;
        private readonly IJobPostService _jobPostService;
        public int Topn { get; }

        public JobPostAggregator(IJobPostService jobPostService, int _topn=5)
        {
            jpc = new JobPostCollection();
            Topn = _topn;
            _jobPostService = jobPostService;
        }

        public Dictionary<string, List<string>> RefreshJobPosts()
        {
            var jobPosts = _jobPostService.GetAllJobsFromQueue();
            foreach (var newjp in jobPosts)
            {
                InsertJobPost(newjp);
            }
            return CollectTopNSkills();
        }
        public void InsertJobPost(JobPost jp)
        {
            if (jp == null)
            {
                return;
            }
            jpc.InsertNewJobPost(jp);
        }

        private Dictionary<string, List<string>> CollectTopNSkills()
        {
            List<KeyValuePair<string, SortedSkillList>> keyValuePairs = jpc.GetSkillsForEachQuery();
            var dpt = new Dictionary<string, List<string>>();
            foreach (var item in keyValuePairs)
            {
                dpt[item.Key] = item.Value.GetTopNSkills(Topn);
            }
            return dpt;
        }

    }

    internal class JobPostCollection
    {
        private readonly HashSet<JobPost> UniqueJobPost = new HashSet<JobPost>();
        private Dictionary<string, SortedSkillList> QueryStringToTopNSkills = new Dictionary<string, SortedSkillList>(); // queryParam -> Dictionary<skill, count>

        public JobPostCollection() {
        }

        public void InsertNewJobPost(JobPost jp)
        {
            if(!UniqueJobPost.Contains(jp))
            {
               UniqueJobPost.Add(jp);
                if (jp.QueryParameter != null) // no need but okay
                {
                    if (!QueryStringToTopNSkills.ContainsKey(jp.QueryParameter)) // no need but okay
                    {
                        QueryStringToTopNSkills[jp.QueryParameter] = new SortedSkillList();
                    }
                    foreach (var skill in jp.SkillsTags)
                    {
                        QueryStringToTopNSkills[jp.QueryParameter].InsertSkill(skill);
                    }
                }
            }
            return;
        }

        public List<KeyValuePair<string, SortedSkillList>> GetSkillsForEachQuery()
        {
            var topSkillsForEachQuery = new List<KeyValuePair<string, SortedSkillList>>();
            foreach (var item in QueryStringToTopNSkills)
            {
                KeyValuePair <string, SortedSkillList> pairItem = new KeyValuePair<string, SortedSkillList>(item.Key, item.Value);
                topSkillsForEachQuery.Add(pairItem);
            }
            return topSkillsForEachQuery;
        }

    }

    internal class SortedSkillList
    {
        private SortedList<string, int> SkillList = new SortedList<string, int>();
        public SortedSkillList()
        {
        }
        public void InsertSkill(string skill)
        {
            if (SkillList.ContainsKey(skill))
            {
                SkillList[skill] += 1;
            }
            else
            {
                SkillList[skill] = 1;
            }
        }
        public List<string> GetTopNSkills(int n)
        {
            List<string> topSkills = new List<string>();
            foreach (var skill in SkillList.OrderByDescending(key => key.Value).Take(n))
            {
                topSkills.Add(skill.Key);
            }
            return topSkills;
        }
    }
}
