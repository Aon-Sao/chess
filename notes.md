# Staff notes
I started this project in a different semester. I am now updating the tests to the Spring 25 semester. To make sure my project is clean, I will be deleting my work and then re-adding parts of it as they become necessary for the tests. This is happening on a branch I plan to merge into main, where all this code was previously developed. If there are any problems with my git history please contact me. Fwiw there are also some local branches that I never merged or gave remotes on GitHub, but I don't think I use any code that was uniquely developed on those branches.

# My notes
Whenever I instructed Intellij to run the BishopMoveTests using a clean copy of the template .idea/ folder, junit failed to discover the test classes. After running `mvn clean test` these classes are found fine.

Maven does not do the least surprising thing. I know we are learning maven later. I'll reconfigure not to use it.

# TODO
* Verify copy constructor for ChessBoard copies grid
* Verify copy constructor for ChessPiece copies maxMoveDistance
