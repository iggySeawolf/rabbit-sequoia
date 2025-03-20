namespace RabbitServices.Models
{
    //{"queryParameter":"resume:.NET","companyName":"Outreach.io","title":"Senior Executive Assistant",
    //"skillsTags":["Artificial Intelligence","Decision-Making","Marketing Strategy","Marketing","Follow through","SAP","Sales Compensation","Okta","Revenue Cycle"],
    //"jobPostedWhen":"2025-03-14T23:25:03.527Z","messagePublishedOn":"2025-03-17T04:28:00.504Z"}
    public class JobPost
    {
        public string? QueryParameter{ get; set; }
        public string? CompanyName{ get; set; }
        public string? Title{ get; set; }
        public List<string>? SkillsTags { get; set; }
        public DateTime? JobPostedWhen { get; set; }

        public DateTime? MessagePublishedOn { get; set; }

        public override bool Equals(object? obj)
        {
            if (obj is JobPost other)
            {
                return QueryParameter == other.QueryParameter &&
                       CompanyName == other.CompanyName &&
                       Title == other.Title &&
                       JobPostedWhen == other.JobPostedWhen &&
                       MessagePublishedOn == other.MessagePublishedOn;
            }
            return false;
        }

        public bool Equals(JobPost? other)
        {
            if (other == null)
                return false;

            return QueryParameter == other.QueryParameter &&
                   CompanyName == other.CompanyName &&
                   Title == other.Title &&
                   JobPostedWhen == other.JobPostedWhen &&
                   MessagePublishedOn == other.MessagePublishedOn;
        }

        public override int GetHashCode()
        {
            return HashCode.Combine(QueryParameter, CompanyName, Title, JobPostedWhen, MessagePublishedOn);
        }

    }
}
