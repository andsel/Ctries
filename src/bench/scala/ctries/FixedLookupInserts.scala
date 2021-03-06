package ctries



import Global._
import scala.testing.Benchmark



object FixedLookupInsertsCHM extends Benchmark {
  import java.util.concurrent.ConcurrentHashMap
  val chm = new ConcurrentHashMap[Elem, Elem]
  
  def run() {
    val p = par.get
    val step = sz / p
    
    val ins = for (i <- 0 until p) yield new Worker(chm, i, step)
    
    for (i <- ins) i.start()
    for (i <- ins) i.join()
  }
  
  class Worker(chm: ConcurrentHashMap[Elem, Elem], n: Int, step: Int) extends Thread {
    override def run() {
      val ratio = lookupratio.get
      var i = n * step
      val until = (n + 1) * step
      val e = elems
      while (i < until) {
        // do an insert
        chm.put(e(i), e(i))
        i += 1
        
        // do some lookups
        var j = 0
        while (j < ratio) {
          chm.get(e(math.abs(j * 0x9e3775cd) % i))
          j += 1
        }
      }
    }
  }
}


object FixedLookupInsertsCtrie extends Benchmark {
  val ct = new ctries.ConcurrentTrie[Elem, Elem]
  
  def run() {
    val p = par.get
    val step = sz / p
    
    val ins = for (i <- 0 until p) yield new Worker(ct, i, step)
    
    for (i <- ins) i.start()
    for (i <- ins) i.join()
  }
  
  class Worker(ct: ctries.ConcurrentTrie[Elem, Elem], n: Int, step: Int) extends Thread {
    override def run() {
      val ratio = lookupratio.get
      var i = n * step
      val until = (n + 1) * step
      val e = elems
      while (i < until) {
        // do an insert
        ct.insert(e(i), e(i))
        i += 1
        
        // do some lookups
        var j = 0
        while (j < ratio) {
          ct.lookup(e(math.abs(j * 0x9e3775cd) % i))
          j += 1
        }
      }
    }
  }
}


object FixedLookupUpdatesCtrie2 extends Benchmark {
  val ct = new ctries2.ConcurrentTrie[Elem, Elem]
  
  def run() {
    val p = par.get
    val step = sz / p
    
    val ins = for (i <- 0 until p) yield new Worker(ct, i, step)
    
    for (i <- ins) i.start()
    for (i <- ins) i.join()
  }
  
  class Worker(ct: ctries2.ConcurrentTrie[Elem, Elem], n: Int, step: Int) extends Thread {
    override def run() {
      val ratio = lookupratio.get
      var i = n * step
      val until = (n + 1) * step
      val e = elems
      while (i < until) {
        // do an update
        ct.update(e(i), e(i))
        i += 1
        
        // do some lookups
        var j = 0
        while (j < ratio) {
          ct.lookup(e(math.abs(j * 0x9e3775cd) % i))
          j += 1
        }
      }
    }
  }
}

