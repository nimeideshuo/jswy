CREATE TABLE IF NOT EXISTS "cu_customer" (
"id"   TEXT(50) NOT NULL,
"name"  TEXT(50),
"pinyin" TEXT(50) ,
"orderno" INTEGER,
"contact"  TEXT(50),
"contactmoblie" TEXT(50) ,
"telephone" TEXT(50) ,
"regionid" TEXT(50),
"customertypeid" TEXT(50),
"visitlineid" TEXT(50),
"depositbank" TEXT(50),
"bankingaccount" TEXT(50),
"promotionid" TEXT(20),
"address" TEXT(50) ,
"pricesystemid" TEXT(1),
"longitude" TEXT(20),
"latitude" TEXT(20),
"remark" TEXT(50),
"isnew" TEXT(1),
"isfinish" TEXT(1),
"exhibitionterm"	TEXT(5),
"lastexhibition"  	TEXT(20),
"isavailable" 		TEXT(1),
"isusecustomerprice"    TEXT(1),
PRIMARY KEY("id","isnew")
);


CREATE TABLE IF NOT EXISTS "cu_customertype" (
"id" TEXT(50) NOT NULL,
"name" TEXT(50),
"pricesystemid" TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "sz_region" (
"id" TEXT(50) NOT NULL,
"name" TEXT(50),
"pinyin" TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY("id")
); 

CREATE TABLE IF NOT EXISTS "sz_visitline" (
"id" TEXT(50) NOT NULL,
"name" TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY("id")
); 

CREATE TABLE IF NOT EXISTS "sz_account"(
"aid"    TEXT(50) NOT NULL,
"aname" TEXT(50),
"pinyin" 	text(50),
"parentaccountid" TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY("aid")
);

CREATE TABLE IF NOT EXISTS "sz_promotion"(
"id" TEXT(50) NOT NULL,
"name" TEXT(50),
"begintime" TEXT(50),
"endtime" TEXT(50),
PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "sz_promotiongoods"(
"promotionid" TEXT(50) NOT NULL,
"goodsid" TEXT(50) NOT NULL,
"unitid" TEXT(20),
"type" TEXT(1),
"giftgoodsid" TEXT(50),
"giftunitid" TEXT(50),
"num" TEXT(50),
"giftnum" TEXT(50),
"initnum" TEXT(20),
"leftnum" TEXT(20),
"price" TEXT(20),
"summary" TEXT(100),
PRIMARY KEY("promotionid","goodsid")
);

CREATE TABLE IF NOT EXISTS "sz_department" (
"did"   TEXT(50) NOT NULL,
"dname"  TEXT(50),
"warehouseid" TEXT(50),
"warehousename" TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY ("did")
);


CREATE TABLE IF NOT EXISTS "sz_warehouse" (
"id"   TEXT(50) NOT NULL,
"name"  TEXT(50),
"pinyin"  TEXT(50),
"istruck"  TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "sz_paytype" (
"id"   TEXT(50) NOT NULL,
"name"  TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "cw_receiveincome" (
"id"   TEXT(50) NOT NULL,
"name"   TEXT(50) NOT NULL,
PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "kf_picture" (
"id"  INTEGER NOT NULL,
"docid" INTEGER,
"picturepath" TEXT(100),
"takeperson" TEXT(20),
"taketime"  TEXT(20),
"docrid"    INTEGER,
PRIMARY KEY ("id","docid")
);

CREATE TABLE IF NOT EXISTS "kf_fieldsale" (
"id"  			INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
"customerid"  		nvarchar(100),
"isnewcustomer"		TEXT(1),
"showid"		nvarchar(100),
"customername"  	TEXT(20),
"departmentid" 	 	TEXT(20),
"departmentname"  	TEXT(50),
"warehouseid"  		TEXT(20),
"warehousename"  	TEXT(50),
"pricesystemid"  	TEXT(20),
"promotionid"    	TEXT(50),
"preference"		nvarchar(100),
"status" 		integer,
"remark"  		TEXT(20),
"builderid"  		TEXT(20),
"buildername"  		TEXT(20),
"buildtime"  		TEXT(20),
"printnum"  		TEXT(20),
"longitude" TEXT(50),
"latitude" TEXT(50),
"address" TEXT(50)
);

CREATE TABLE IF NOT EXISTS "kf_fieldsaleitem" (
"serialid"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
"fieldsaleid"  		INTEGER NOT NULL,
"warehouseid"		TEXT(20),
"goodsid"  		TEXT(20),
"saleunitid"  		TEXT(20),
"salenum"  		DECIMAL(19,4),
"saleprice"  		DECIMAL(19,4),
"saleremark"		TEXT(50),
"givenum"  		DECIMAL(19,4),
"giveunitid" 		TEXT(20),
"giveremark"		TEXT(50),
"cancelbasenum"		DECIMAL(19,4),
"cancelremark"		TEXT(50),
"remark"		TEXT(20),
"giftgoodsid"		TEXT(20),
"giftgoodsname"		TEXT(20),
"giftunitid"		TEXT(20),
"giftunitname"		TEXT(20),
"giftnum"		DECIMAL(19,4),
"giftremark"		TEXT(50),
"promotiontype"		TEXT(20),	
"ispromotion" 		TEXT(1),
"isexhibition"		TEXT(1)
);

CREATE TABLE IF NOT EXISTS "kf_fieldsaleitembatch" (
"serialid"		INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
"fieldsaleid"	TEXT(20) NOT NULL,
"goodsid"	TEXT(20) NOT NULL,
"batch"			TEXT(20),
"productiondate"	TEXT(20),
"isout"		TEXT(1),
"unitid" 		TEXT(20),
"price"		DECIMAL(19,4),
"num"			TEXT(20)		
);

CREATE TABLE IF NOT EXISTS "kf_fieldsalepaytype"(
"serialid"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
"fieldsaleid" TEXT(50),
"paytypeid" TEXT(50),
"paytypename" TEXT(50),
"amount" DECIMAL(19,4)
);
      
CREATE TABLE IF NOT EXISTS "kf_transferdoc" (
"id"  			INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
"showid"		nvarchar(100),
"departmentid" 	 	TEXT(20),
"departmentname"  	TEXT(50),
"inwarehouseid"  	TEXT(20),
"inwarehousename"  	TEXT(50),
"isposted" 		TEXT(1),
"isupload"		TEXT(1),
"remark"  		TEXT(20),
"builderid"  		TEXT(20),
"buildername"  		TEXT(20),
"buildtime"  		TEXT(20)
);
      
CREATE TABLE IF NOT EXISTS "kf_transferitem" (
"serialid"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
"transferdocid"		INTEGER NOT NULL,
"warehouseid"		TEXT(20),
"goodsid"  		TEXT(20),
"unitid"  		TEXT(20),
"num"			DECIMAL(19,4),
"batch"				TEXT(20),
"productiondate"	TEXT(20),
"remark"			TEXT(20)
);

CREATE TABLE IF NOT EXISTS "cu_customerfieldsalegoods" (
"customerid"  TEXT(20) NOT NULL,
"goodsid"  TEXT(50) NOT NULL,
"goodsthirdclassid" TEXT(50),
"issale"  TEXT(50),
"ispass"  TEXT(50),
"unitid"  TEXT(50),
"price"  TEXT(50),
PRIMARY KEY ("customerid", "goodsid")
);

CREATE TABLE IF NOT EXISTS "sz_goods" (
"id"  TEXT(50) NOT NULL,
"name"  TEXT(50),
"pinyin"  TEXT(50),
"barcode"  TEXT(50),
"salecue"  TEXT(400),
"specification"  TEXT(50),
"model" TEXT(50),
"isusebatch" TEXT(1),
"goodsclassid" TEXT(50),
"goodsclassname" TEXT(50),
"stocknumber" TEXT(50),
"bigstocknumber" TEXT(50),
"getstocktime" TEXT(50),
"initnumber" TEXT(50),
"biginitnumber" TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "kf_goodsbatch" (
"goodsid"  TEXT(50) NOT NULL,
"batch"  TEXT(50),
"productiondate"  TEXT(50),
"stocknumber"  TEXT(50),
"bigstocknumber" TEXT(50),
PRIMARY KEY ("goodsid", "batch")
);

CREATE TABLE IF NOT EXISTS "sz_unit" (
"id"  TEXT(50) NOT NULL,
"name"  TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "sz_goodsunit" (
"goodsid"  TEXT(50) NOT NULL,
"unitid"  TEXT(50) NOT NULL,
"unitname"  TEXT(50) ,
"isbasic"  TEXT(10),
"isshow"  TEXT(10),
"ratio"  TEXT(50),
PRIMARY KEY ("goodsid", "unitid")
);

CREATE TABLE IF NOT EXISTS "sz_goodsprice" (
"goodsid"  TEXT(50) NOT NULL,
"unitid"  TEXT(50) NOT NULL,
"unitname"  TEXT(50) ,
"pricesystemid"  TEXT(50) NOT NULL,
"pricesystemname"  TEXT(50),
"price"  TEXT(50),
PRIMARY KEY ("goodsid", "unitid", "pricesystemid")
);

CREATE TABLE IF NOT EXISTS "sz_goodsimage" (
"serialid"  TEXT(50) NOT NULL,
"goodsid"  TEXT(50),
"imagepath"  TEXT(50),
"isgot"  TEXT(50),
PRIMARY KEY ("serialid")
);

CREATE TABLE IF NOT EXISTS "sz_pricesystem" (
"psid"  TEXT(50) NOT NULL,
"psname"  TEXT(50),
"pinyin"  TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY ("psid")
);

CREATE TABLE IF NOT EXISTS "cw_settleup" (
"id"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
"departmentid"  TEXT(50),
"departmentname"  TEXT(50),
"objectid"  TEXT(50),
"isnewobject"	TEXT(1),
"objectname"  TEXT(50),
"objectoperator"  TEXT(50),
"preference" REAL(19,4),
"issubmit"  TEXT(1),
"remark"  TEXT(200),
"builderid"  TEXT(50),
"buildername"  TEXT(50),
"buildtime"  TEXT,
"type" TEXT(4)
);

CREATE TABLE IF NOT EXISTS "cw_settleupitem" (
"serialid"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
"settleupid"  INTEGER,
"doctime"  TEXT,
"docid"  INTEGER,
"docshowid" TEXT(20),
"doctype" TEXT(1),
"doctypename" TEXT(20),
"receivableamount"  TEXT(20),
"receivedamount" TEXT(20),
"leftamount"  TEXT(20),
"thisamount"  DECIMAL(19,4)
);

CREATE TABLE IF NOT EXISTS "cw_othersettleupitem" (
"serialid"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
"othersettleupid"  INTEGER,
"accountid" TEXT(50),
"accountname" TEXT(50),
"amount"  DECIMAL(19,4)
);

CREATE TABLE IF NOT EXISTS "cw_settleuppaytype" (
"id"  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
"settleupid"  INTEGER,
"paytypeid"  TEXT(50),
"paytypename"  TEXT(50),
"amount"  DECIMAL(19,4),
"remark"  TEXT(400)
);

CREATE TABLE IF NOT EXISTS "kf_fieldsaleimage" (
"serialid" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
"fieldsaleid"  INTEGER,
"issignature" TEXT(1),
"imagepath"  TEXT(100),
"remark"  TEXT(400)
);

CREATE TABLE IF NOT EXISTS "kf_serverdoc"(
"docid"         TEXT(50),
"docshowid"     TEXT(50),
"customerid"	TEXT(10),
"customername"  TEXT(20),
"doctype"       TEXT(10),
"doctypename"       TEXT(20),
"receivableamount"  REAL(19,4),
"receivedamount"    REAL(19,4),
"leftamount"	    REAL(19,4),
"doctime"       TEXT(50),
PRIMARY KEY("docid","docshowid")
);

CREATE TABLE IF NOT EXISTS "sz_user" (
"id" TEXT(50) NOT NULL,
"name" TEXT(50),
"pinyin" TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY("id")
);