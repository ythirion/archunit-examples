using System;
using ArchUnit.Demo.Examples.Models;
using Microsoft.AspNetCore.Mvc;

namespace ArchUnit.Demo.Examples.Controllers
{
    [ApiController]
    public class Controller
    {
        public ApiResponse<int> Matching() => new ApiResponse<int>(42);
        public void NotMatching() { }
        public int Universe() => 42;
    }
}