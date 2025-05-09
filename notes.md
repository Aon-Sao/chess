# Staff notes
I started this project in a different semester. I am now updating the tests to the Spring 25 semester. To make sure my project is clean, I will be deleting my work and then re-adding parts of it as they become necessary for the tests. This is happening on a branch I plan to merge into main, where all this code was previously developed. If there are any problems with my git history please contact me. Fwiw there are also some local branches that I never merged or gave remotes on GitHub, but I don't think I use any code that was uniquely developed on those branches.

# My notes
Whenever I instructed Intellij to run the BishopMoveTests using a clean copy of the template .idea/ folder, junit failed to discover the test classes. After running `mvn clean test` these classes are found fine.

Maven does not do the least surprising thing. I know we are learning maven later. I'll reconfigure not to use it.

## Singleton DAOs
According to my research, there are better approaches than making my DAO implementations use the singleton pattern. I could make everything static in the DAO, essentially using it just as a namespace; except I am required to have DAO interfaces, and this is not compatibile. I could use a dependency injection framework, like Spring; but I am not allowed to add dependencies. So I will leave my singleton patterns be and stop trying to refactor. I could hide the getInstance calls, but every public instance method would need to delegate to a private static method, which adds lots of boilerplate. Perhaps where the actual data stores (memory, DB) are static/single, it doesn't matter whether the DAO instances interacting with them are also single, so long as there is no monkey-patching.

# TODO
* Verify copy constructor for ChessBoard copies grid
* Verify copy constructor for ChessPiece copies maxMoveDistance
