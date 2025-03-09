using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RabbitServices
{
    public interface IRabbitService
    {
        public void Receive();
    }

}
