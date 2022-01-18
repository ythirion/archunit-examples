using System;
namespace ArchUnit.Demo.Examples
{
    public interface ICommandHandler<TCommand>
        where TCommand : Command
    {
        int Handle(TCommand command);
    }
}