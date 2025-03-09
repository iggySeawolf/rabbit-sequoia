namespace dotnet_section.Models
{
    //{"queryParameter":".NET","skillsTags":[".NET","SQl","Linux","React"],"messagePublishedOn":"2025-03-09T19:22:48.000222572"}
    public class JobPost
    {
        public string? QueryParameter{ get; set; }
        public List<string>? SkillsTags { get; set; }
        public DateTime? MessagePublishedOn { get; }
    }
}
