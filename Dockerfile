FROM osgeo/gdal:latest

ENV HOME=/home
ENV SBT_OPTS="-Xms2G -Xmx4G -Xss4M --supershell=false -Duser.home=/home"

RUN wget https://github.com/sbt/sbt/releases/download/v1.5.5/sbt-1.5.5.tgz
RUN tar -xf sbt-1.5.5.tgz -C /usr/local
RUN ln -s /usr/local/sbt/bin/sbt /usr/local/bin/sbt

RUN rm sbt-1.5.5.tgz

