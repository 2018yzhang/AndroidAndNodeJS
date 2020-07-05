const express = require('express');
const app = express(),
      bodyParser = require("body-parser");
var request = require("request");
const users = [];

app.use(bodyParser.json());
//app.use(express.static(process.cwd()+"/my-app/dist/angular-nodejs-project/"));

app.get('/api/details', (req, res) => {
  var itemId = req.query.itemId
  var url = 'http://open.api.ebay.com/shopping?callname=GetSingleItem&responseencoding=JSON&appid=YidanZha-Shopping-PRD-12eb6be68-9759c54c&siteid=0&version=967&ItemID='+itemId+'&IncludeSelector=Description,Details,ItemSpecifics'
  var result = request(url)
  req.pipe(result).pipe(res)
});

app.get('/api/item', (req, res) => {
  var filterNum = 0;
  var keywords = req.query.keywords
  var sortOrder = req.query.sortOrder
  //var url = 'https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=YidanZha-Shopping-PRD-12eb6be68-9759c54c&RESPONSE-DATA-FORMAT=JSON&keywords=iphone&paginationInput.entriesPerPage=100&sortOrder=BestMatch';
  var url = 'https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=YidanZha-Shopping-PRD-12eb6be68-9759c54c&RESPONSE-DATA-FORMAT=JSON&keywords='+keywords+'&paginationInput.entriesPerPage=100&sortOrder='+sortOrder;
  var maxprice = req.query.MaxPrice
  if(maxprice!=''){
    url = url + '&itemFilter(' + filterNum.toString() + ').name=MaxPrice&itemFilter(' + filterNum.toString() + ').value=' + maxprice + '&itemFilter(' + filterNum.toString() + ').paramName=Currency&itemFilter(' + filterNum.toString() + ').paramValue=USD'
    filterNum += 1
  }
  var minprice = req.query.MinPrice
  if(minprice!=''){
    url += '&itemFilter(' + filterNum.toString() + ').name=MinPrice&itemFilter(' + filterNum.toString() + ').value=' + minprice + '&itemFilter(' + filterNum.toString() + ').paramName=Currency&itemFilter(' + filterNum.toString() + ').paramValue=USD'
    filterNum += 1
  }
  var tempurl = '&itemFilter(' + filterNum.toString() + ').name=Condition';
  var valuenum =0
  var newP = req.query.New
  if(newP!=''){
    tempurl += '&itemFilter(' + filterNum.toString() + ').value(' + valuenum.toString() + ')=' + newP;
    valuenum += 1
  }
  var used = req.query.Used
  
  if(used!=''){
    tempurl += '&itemFilter(' + filterNum.toString() + ').value(' + valuenum.toString() + ')=' + used;
    valuenum += 1
  }
  
  var unspecified = req.query.Unspecified
  if(unspecified!=''){
    tempurl += '&itemFilter(' + filterNum.toString() + ').value(' + valuenum.toString() + ')='+ unspecified
    valuenum += 1
  }
  if(valuenum>0){
    url = url+tempurl;
    filterNum += 1
  }
  /**
   * var filterNum = 0;

  var keywords = req.query.keywords
  var sortOrder = req.query.sortOrder
  url = url + '&keywords=' + keywords + '&sortOrder=' + sortOrder;
  var maxprice = req.query.MaxPrice
  if(maxprice!=''){
    url = url + '&itemFilter(' + filterNum.toString() + ').name=MaxPrice&itemFilter(' + filterNum.toString() + ').value=' + maxprice + '&itemFilter(' + filterNum.toString() + ').paramName=Currency&itemFilter(' + filterNum.toString() + ').paramValue=USD'
    filterNum += 1
  }
  ///item?keywords=iphone&MaxPrice=&MinPrice=&new=&used=&veryGood=&good=&acceptable=&ReturnsAcceptedOnly=false&FreeShippingOnly=false&ExpeditedShippingType=&sortOrder=
  var minprice = req.query.MinPrice
  if(minprice!=''){
    url += '&itemFilter(' + filterNum.toString() + ').name=MinPrice&itemFilter(' + filterNum.toString() + ').value=' + minprice + '&itemFilter(' + filterNum.toString() + ').paramName=Currency&itemFilter(' + filterNum.toString() + ').paramValue=USD'
    filterNum += 1
  }

  var tempurl = '&itemFilter(' + filterNum.toString() + ').name=Condition';
  var valuenum =0
  var newP = req.query.new
  if(newP!=''){
    tempurl += '&itemFilter(' + filterNum.toString() + ').value(' + valuenum.toString() + ')=' + newP;
    valuenum += 1
  }
  var used = req.query.used
  
  if(used!=''){
    tempurl += '&itemFilter(' + filterNum.toString() + ').value(' + valuenum.toString() + ')=' + used;
    valuenum += 1
  }
  
  var veryGood = req.query.veryGood
  if(veryGood!=''){
    tempurl += '&itemFilter(' + filterNum.toString() + ').value(' + valuenum.toString() + ')='+ veryGood
    valuenum += 1
  }
  var good = req.query.good
  if(good!=''){
    tempurl += '&itemFilter(' + filterNum.toString() + ').value(' + valuenum.toString() + ')='+ good
    valuenum += 1
  }
  var acceptable = req.query.acceptable
  if(acceptable!=''){
    tempurl += '&itemFilter(' + filterNum.toString() + ').value(' + valuenum.toString() + ')='+ acceptable
    valuenum += 1
  }
  if(valuenum>0){
    url = url+tempurl;
    filterNum += 1
  }
  var ReturnsAcceptedOnly = req.query.ReturnsAcceptedOnly;
  if(ReturnsAcceptedOnly=='true'){
    url += '&itemFilter(' + filterNum.toString() + ').name=ReturnsAcceptedOnly&itemFilter(' + filterNum.toString() + ').value=' +ReturnsAcceptedOnly
        filterNum += 1
  }
  
  var FreeShippingOnly = req.query.FreeShippingOnly;
  if (FreeShippingOnly == 'true'){
    url += '&itemFilter(' + filterNum.toString() + ').name=FreeShippingOnly&itemFilter(' + filterNum.toString() + ').value='+ FreeShippingOnly
    filterNum += 1
  }
  var ExpeditedShippingType = req.query.ExpeditedShippingType;
    if (ExpeditedShippingType == 'Expedited'){
      url += '&itemFilter(' + filterNum.toString() + ').name=ExpeditedShippingType&itemFilter(' + filterNum.toString() + ').value=' + ExpeditedShippingType
      filterNum += 1
    }
  //console.log("query" +req.query.keywords)
  //users.push(user);
  //var url = 'https://svcs.ebay.com/services/search/FindingService/v1?OPERATION-NAME=findItemsAdvanced&SERVICE-VERSION=1.0.0&SECURITY-APPNAME=YidanZha-Shopping-PRD-12eb6be68-9759c54c&RESPONSE-DATA-FORMAT=JSON&REST-PAYLOAD&keywords='+keywords+'&paginationInput.entriesPerPage=100&sortOrder=BestMatch&itemFilter(0).name=MaxPrice&itemFilter(0).value=25&itemFilter(0).paramName=Currency&itemFilter(0).paramValue=USD'
  console.log(url)
   */
  
  var result = request(url)
  //console.log(result)
  //console.log(typeof req.pipe(result))
  //console.log(req.pipe(result)[0])
  //console.log(typeof req.pipe(result).pipe(res))
  //console.log(req.pipe(result).pipe(res)[0])
  //console.log("result-------->"+json(result))
  req.pipe(result).pipe(res)
  /**
   * var resultCount = result["findItemsAdvancedResponse"][0]["paginationOutput"][0]["totalEntries"][0]
    if (resultCount=='0'){
      res.json({"key": keywords,"resultCount": resultCount})
    }
    else{
      res.json({"key": keywords,"resultCount": resultCount, "results": result});
    }
   */
});
/**
 * app.get('/', (req,res) => {
  res.sendFile(process.cwd()+"/my-app/dist/angular-nodejs-project/index.html")
});
 */


const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`App listening on port ${PORT}`);
  console.log('Press Ctrl+C to quit.');
});
// [END gae_node_request_example]

module.exports = app;
