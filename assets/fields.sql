CREATE TABLE IF NOT EXISTS "sz_user"(
"id" 			TEXT(50) NOT NULL,
"name"			TEXT(50),
PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "cu_customer"(
"id" 			TEXT(50) NOT NULL,
"name"			TEXT(50),
"pinyin"		TEXT(50),
"contactmoblie" 	TEXT(50),
"address"		TEXT(50),
"settleterm"		TEXT(50),
"discountratio"		TEXT(50),
"promotionid"		TEXT(50),
"promotionname"		TEXT(50),
"pricesystemid" 	TEXT(50),
"isavailable" 		TEXT(1),
"iscustomer" 	TEXT(50),
"issupplier" 	TEXT(50),
PRIMARY KEY("id")
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
"istruck"  TEXT(1),
"isavailable" 		TEXT(1),
PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "sz_paytype" (
"id"   TEXT(50) NOT NULL,
"name"  TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "sz_goods" (
"id"  			TEXT(50) NOT NULL,
"name"  		TEXT(50),
"pinyin"  		TEXT(50),
"barcode"  		TEXT(50),
"salecue"  		TEXT(400),
"specification"  	TEXT(50),
"model" 		TEXT(50),
"goodsclassid" 		TEXT(50),
"goodsclassname" 	TEXT(50),
"stocknumber" 		DECIMAL(38,2),
"bigstocknumber"	TEXT(50),
"getstocktime"		TEXT(50),
"isavailable" 		TEXT(1),
"isusebatch"		TEXT(1),
PRIMARY KEY ("id")
);

CREATE TABLE IF NOT EXISTS "sz_goodsunit" (
"goodsid"  		TEXT(50) NOT NULL,
"unitid"  		TEXT(50) NOT NULL,
"unitname"  		TEXT(50) ,
"isbasic"  		TEXT(1),
"isshow"  		TEXT(1),
"ratio"  		DECIMAL(38,2),
PRIMARY KEY ("goodsid", "unitid")
);

CREATE TABLE IF NOT EXISTS "sz_goodsimage" (
"serialid"  TEXT(50) NOT NULL,
"goodsid"  TEXT(50),
"imagepath"  TEXT(50),
"isgot"  TEXT(50),
PRIMARY KEY ("serialid")
);

CREATE TABLE IF NOT EXISTS "sz_visitline" (
"id" TEXT(50) NOT NULL,
"name" TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY("id")
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

CREATE TABLE IF NOT EXISTS "sz_user" (
"id" TEXT(50) NOT NULL,
"name" TEXT(50),
"pinyin" TEXT(50),
"isavailable" 		TEXT(1),
PRIMARY KEY("id")
);

CREATE TABLE IF NOT EXISTS "sz_goodsclass" (
"id" TEXT(50) NOT NULL,
"name"  TEXT(50),
"pinyin" TEXT(50),
"parentgoodsclassid"  TEXT(50),
PRIMARY KEY("id")
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


