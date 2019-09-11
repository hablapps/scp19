package optica
package concrete

import scalaz._, Scalaz._

case class AffineFold[S, A](preview: S => Option[A])

object AffineFold {

  implicit object AffineFoldCategory extends Category[AffineFold] {

    def id[A]: AffineFold[A, A] = AffineFold(a => Some(a))

    def compose[A, B, C](
        f: AffineFold[B, C],
        g: AffineFold[A, B]): AffineFold[A, C] =
      AffineFold(a => g.preview(a) >>= f.preview)
  }

  def filtered[S](p: Getter[S, Boolean]): AffineFold[S, S] =
    AffineFold(s => if (p.get(s)) Option(s) else None)

  implicit def fromGetter[S, A](gt: Getter[S, A]): AffineFold[S, A] =
    AffineFold(gt.get(_).point[Option])
}

