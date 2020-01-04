#!/bin/bash
mvn compile
mvn exec:java -Dexec.mainClass="com.jetbrains.Server.Servidor"
