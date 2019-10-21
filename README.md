# 日志收集系统

## 系统简介

分布式日志收集和处理系统，通过shell远程读取服务器上的日志信息，解析后发送到elasticsearch和prometheus，通过grafana的api自动新增监控指标，并向钉钉发送报警信息。

## 主要特点

所有操作都在web界面上进行，通过ssh协议实现对所有服务器的操作，自动将日志收集程序分发到多台机器进行执行。

## 用到技术

后台：springboot，mybatis

前端：bootstrap，jquery，vue，adminlte

数据库：mysql

其它：zookeeper，elasticsearch，prometheus，grafana





