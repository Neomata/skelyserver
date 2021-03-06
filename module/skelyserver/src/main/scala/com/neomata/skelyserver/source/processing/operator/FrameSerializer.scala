package com.neomata.skelyserver.source.processing.operator

import akka.stream.scaladsl.Flow
import akka.util.ByteString
import com.neomata.skelyserver.source.processing.OperationStage
import org.bytedeco.javacpp.BytePointer
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.global.{opencv_imgcodecs => ImageCodecs}

object FrameSerializer {
  def apply: FrameSerializer = new FrameSerializer
}

class FrameSerializer extends OperationStage[Mat, ByteString] {
  override def flow: Flow[Mat, ByteString, _]= {
    Flow[Mat].map { frame =>
      val bp = new BytePointer
      ImageCodecs.imencode(".jpg", frame, bp)
      ByteString(bp.getStringBytes)
    }
  }
}
