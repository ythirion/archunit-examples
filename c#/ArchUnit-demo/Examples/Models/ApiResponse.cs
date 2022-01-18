using System;
using System.Collections.ObjectModel;

namespace ArchUnit.Demo.Examples.Models
{
    public record ApiResponse<TData>(TData Data, ApiError[]? Errors = null) { }
}