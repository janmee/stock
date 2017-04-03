/**
 * Created by tim on 15/8/6.
 */
var mysql = require('mysql');
var properties = require('properties');
var async = require('async');
var _ = require('lodash');
var fs = require('fs');

if(process.argv.length !== 5) {
    console.log('usage: node tableToEntity [config.properties path] [dbName] [tableName]');
    process.exit(0);
}

var configPath = '../config/'+process.argv[2];
var dbName = process.argv[3];
var tableName = process.argv[4];

var parseConfig = function (cb) {
    properties.parse(configPath, {path: true}, cb);
};

var getTableDesc = function (conf, cb) {
    var ipReg = /(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]).(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0).(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0).(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])/g;
    var host = ipReg.exec(conf["env.datasource.jdbcUrl"])[0];
    var portReg = /:\d+/g;
    var port = portReg.exec(conf["env.datasource.jdbcUrl"])[0].split(":")[1];
    console.log(host);
    console.log(port);
    var connection = mysql.createConnection({
        host: host,
        port: port,
        database: dbName,
        user: conf["env.datasource.username"],
        password: conf["env.datasource.password"]
    });
    connection.connect(function(error) {
        if(error) {
            console.log("error");
            cb (error);
        }

        connection.query("show full fields from " + tableName, [], cb);
    });
};

var typeToJava = [
    {
        reg: /^int/,
        type: 'Integer'
    },
    {
        reg: /char\(\d+\)/,
        type: 'String'
    },
    {
        reg: /date/,
        type: 'Date'
    },
    {
        reg: /tinyint/,
        type: 'Boolean'
    },
    {
        reg: /text/,
        type: 'String'
    },
    {
        reg: /smallint/,
        type: 'Integer'
    },
    {
        reg: /double/,
        type: 'Double'
    },
    {
        reg: /bigint/,
        type: 'Long'
    }

];


var genJavaBean = function (fields, defs, cb) {
    console.log(12);
    String.prototype.capitalizeFirstLetter = function() {
        return this.charAt(0).toUpperCase() + this.slice(1);
    };
    String.prototype.lowerFirst = function() {
        return this.charAt(0).toLowerCase() + this.slice(1);
    };
    var template = fs.readFileSync('./tableToEntity.tmpl', 'utf8');
    var compiled = _.template(template);
    console.log(fields);
    _.forEach(fields, function(field){

        _.forEach(typeToJava, function(type){
            if(type.reg.test(field.Type)){
                field.JavaType = type.type;
                return false;
            }
        });

        field.JavaField = _.camelCase(field.Field.replace(/^[a-z]_/, ''));
    });
    var result = compiled({
        tableName: tableName,
        className: _.camelCase(tableName.replace(/^t_/, '')).capitalizeFirstLetter(),
        columns: fields
    });
    // console.log(result);
    var className = _.camelCase(tableName.replace(/^t_/, '')).capitalizeFirstLetter();
    fs.writeFileSync('../../java/com/janmee/stock/entity/'+className+'.java', result);

    //生成DAO
    template = fs.readFileSync('./tableToDao.tmpl','utf8');
    compiled = _.template(template);
    _.forEach(fields, function(field){
        _.forEach(typeToJava, function(type){
            if(type.reg.test(field.Type)){
                field.JavaType = type.type;
                return false;
            }
        });

        field.JavaField = _.camelCase(field.Field.replace(/^[a-z]_/, ''));
    });
    result = compiled({
        tableName: tableName,
        className: _.camelCase(tableName.replace(/^t_/, '')).capitalizeFirstLetter(),
        columns: fields
    });
    //console.log(result);
    fs.writeFileSync('../../java/com/janmee/stock/dao/'+className+'Dao.java', result);

    //service
    template = fs.readFileSync('./tableToService.tmpl','utf8');
    compiled = _.template(template);
    _.forEach(fields, function(field){
        _.forEach(typeToJava, function(type){
            if(type.reg.test(field.Type)){
                field.JavaType = type.type;
                return false;
            }
        });

        field.JavaField = _.camelCase(field.Field.replace(/^[a-z]_/, ''));
    });
    result = compiled({
        tableName: tableName,
        className: _.camelCase(tableName.replace(/^t_/, '')).capitalizeFirstLetter(),
        columns: fields
    });
    fs.writeFileSync('../../java/com/janmee/stock/service/'+className+'Service.java', result);
    //console.log(result);


    //serviceImpl
    template = fs.readFileSync('./tableToServiceImpl.tmpl','utf8');
    compiled = _.template(template);
    _.forEach(fields, function(field){
        _.forEach(typeToJava, function(type){
            if(type.reg.test(field.Type)){
                field.JavaType = type.type;
                return false;
            }
        });

        field.JavaField = _.camelCase(field.Field.replace(/^[a-z]_/, ''));
    });
    result = compiled({
        tableName: tableName,
        className: _.camelCase(tableName.replace(/^t_/, '')).capitalizeFirstLetter(),
        columns: fields
    });
    fs.writeFileSync('../../java/com/janmee/stock/service/impl/'+className+'ServiceImpl.java', result);
    //console.log(result);

    //controller
    template = fs.readFileSync('./tableToController.tmpl','utf8');
    compiled = _.template(template);
    _.forEach(fields, function(field){
        _.forEach(typeToJava, function(type){
            if(type.reg.test(field.Type)){
                field.JavaType = type.type;
                return false;
            }
        });

        field.JavaField = _.camelCase(field.Field.replace(/^[a-z]_/, ''));
    });
    result = compiled({
        tableName: tableName,
        className: _.camelCase(tableName.replace(/^t_/, '')).capitalizeFirstLetter(),
        columns: fields
    });
    fs.writeFileSync('../../java/com/janmee/stock/controller/'+className+'Controller.java', result);
    //console.log(result);

    //controller
    template = fs.readFileSync('./tableToQuery.tmpl','utf8');
    compiled = _.template(template);
    _.forEach(fields, function(field){
        _.forEach(typeToJava, function(type){
            if(type.reg.test(field.Type)){
                field.JavaType = type.type;
                return false;
            }
        });

        field.JavaField = _.camelCase(field.Field.replace(/^[a-z]_/, ''));
    });
    result = compiled({
        tableName: tableName,
        className: _.camelCase(tableName.replace(/^t_/, '')).capitalizeFirstLetter(),
        columns: fields
    });
    fs.writeFileSync('../../java/com/janmee/stock/vo/query/'+className+'Query.java', result);
    //console.log(result);

    cb();

};



async.waterfall([
    parseConfig,
    getTableDesc,
    genJavaBean
], function(err) {
    if(err){
        console.log(err);
    }

    process.exit(0);
});