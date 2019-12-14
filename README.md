# DigitalReceipts: Splitr

Splitr is an Android app that digitalises information on physical receipts into an digitial itemized version via OCR technology. These digital receipts are stored within the app for ease of storage and retrievial. These digital receipts can then be used for analysis or to supplement other purposes. This iteration of Splitr demonstrates a bill spliting function intergrated with PayLah payment functionality. 

## Features

 - OCR Receipt Processing via Gallery and Camera Functions
 - Categorisation of Purchases
 - Storage and Summary of Transactions
 - Flexible Bill Splitting Function
 - DBS PayLah! Integration with Bill Split Function
 
## User Guide
There are three different tabs: Finance, Camera, and Receipts. 

### Finance
This tab stores all your purchases that you have marked as your own purchase (by either adding to Finance or under the Bill Split function). It provides a quick summary of these purchases by categories using a pie chart. 
![Finance Tab](/readme_images/finance.png)

Our app attempts to categorise all receipts scanned for the nature of the purchase. However, if it is unable to do so, it will default to None. In that case, or if you wish to re-categorise any particular purchase, just click on the purchase and a summary of the purchase will appear. You can then click the Categorise button and type in your own category. 
![Categorise Pop-up](/readme_images/finance_popup.png)

### Camera
This tab opens up a pop-up that prompts you to either upload a receipt from gallery or to take a picture of a receipt. It will then process the receipt uploaded. Once it has been processed, you will notice that the gif will stop. The receipt will then be digitalised and added to your Receipts tab. Do note that this process will take some time and requires an Internet connection. 
For optimal results, the photo of the receipt has to be under good lighting and oriented upright. 
![Camera Tab](/readme_images/camera_popup.png)

### Receipts
This tab stores all past receipts that you have processed before. You can delete any particular receipt by swiping to the left. Upon clicking on any one of the receipts, a pop-up summary of the receipt will appear. You will also be given two options to either 'Add to Finance' or 'Bill Split'. 
![Receipts Tab](/readme_images/receipts.png)
![Receipts Pop-up](/readme_images/receipts_popup.png)
You should click on 'Add to Finance' if the receipt has items that only you paid for. This will add the receipt to the Finance tab. If the receipt has items that you shared with others, you can click on 'Bill Split'. This will bring you to a list of your contacts. Select those you wish to split the receipt with and clicking next will allow you to allocate how much of each item each person will pay for. 
[Selecting contacts and Splitting bill]

Once the receipt has been split, you can choose to send out a SMS to each of these people to request for payment for their respective items. The SMS will include a link to DBS PayLah! for convenience. 
[PayLah SMS]
