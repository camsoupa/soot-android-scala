import com.ucombinator.android.DexplerWrapper
import scala.collection.JavaConversions._
import soot.SootMethod

object Main {
  def main(args: Array[String]) = {

    if (args.length < 2) {
       println("usage:")
       println("  path/to/android/platforms path/to/apk")
       System.exit(0)
    }

    DexplerWrapper.parseApk(args(0), args(1))
    
    /* get the in-app caller methods from the callgraph (excludes library callers ) */
    val callers = DexplerWrapper.getInternalCallers()
    while(callers.hasNext()) {
      println(callers.next())
    }

    val classes = DexplerWrapper.getAst()

    for (clazz <- classes) {
      println(clazz)
    }  
  }
}