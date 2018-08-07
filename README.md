# Elastos.RT Wallet SDK　简要使用说明

## 1. Wallet构件简介
  Elastos.RT中的Wallet构件封装了elastos在github上的Elastos.ELA.SPV.Cpp仓库的功能，可以使用提供的接口创建钱包对象、创建并发送交易等。
  目前，本说明文档仅描述Android平台开发流程，其他平台和环境的文档会随着开发进度同步进行更新。

## 2. Wallet SDK结构
  支持Android平台开发的AAR包：包内包含钱包配置文件、Java接口和一些库文件。

## 3. 使用Wallet SDK的主要步骤
* 注意：调用Wallet接口时需要使用try{}catch(WalletException)，因为如果参数传递不正确，接口实现会抛出WalletException异常给上层

### 3.1 初始化钱包所需的数据
String rootPath = getApplicationContext().getFilesDir().getParent();
ElastosWalletUtils.InitConfig(this, rootPath);

* 创建MasterWalletManager对象时传入的路径，开发者必须保证该目录拥有读写权限。

### 3.2 传递一个可读写的路径，创建 MasterWalletManager
MasterWalletManager　walletManager = new MasterWalletManager(rootPath);

* 创建MasterWalletManager对象时传入的路径，开发者必须保证该目录拥有读写权限，并且路径要与ElastosWalletUtils.InitConfig中的路径相同。

### 3.3 生成助记词
String language = "english";
String mnemonic = walletManager.GenerateMnemonic(language);

* 目前可以支持生成6种语言的助记词：english、chinese、french、italian、japanese和spanish，按需要填入对应的参数。

### 3.4 创建主钱包
String masterWalletId = "masterWalletId";
String phrasePassword = "";
String payPassword = "elastos2018";
IMasterWallet masterWallet = walletManager.CreateMasterWallet(masterWalletId, mnemonic,　phrasePassword, payPassword, language);

* 注意支付密码参数payPassword有一定的要求：不少于8位的字母与数字的组合
* 助记词密码参数phrasePassword可以为空(null)

### 3.5 创建ELA子钱包
* 创建ELA子钱包, 方法CreateSubWallet的chainID参数必须是"ELA"
String chainID = "ELA";
boolean singleAddress = false;
long feePerKb = 10000; //SELA
ISubWallet subWallet = masterWallet.CreateSubWallet(chainID, payPassword, singleAddress, feePerKb);

* 注意：参数feePerKb的单位是SELA

### 3.6 创建主链转账交易
String fromAddress = "";
String toAddress = "the other address";
long amount = 500; //ELA
String memo = "memo";
String remark = "remark";
String transaction = subWallet.CreateTransaction(fromAddress, toAddress, amount, memo, remark);

* 注意：创建主链转账交易时，参数fromAddress可以为空(null);
* 注意：如果子钱包subWallet中的余额小于要转出的金额的话，会抛出WalletException　异常。

### 3.7 计算交易费用
long fee = 0;
fee = subWallet.CalculateTransactionFee(transaction, feePerKb);

* 注意：参数transaction是接口CreateTransaction的返回值

### 3.8 发送交易
String transactionId = subWallet.SendRawTransaction(transaction, fee, payPassword);

* 注意：参数transaction是接口CreateTransaction的返回值

## 4. 辅助接口（主要的）
### 4.1 交易回调
* 注册交易回调，可以获取交易状态，更新界面

subWallet.AddCallback(new ISubWalletCallback() {
    @Override
    public void OnTransactionStatusChanged(String txId, String status, String desc, int confirms) {
        //TOOD 确认数变动会触发该回调函数
    }
    @Override
    public void OnBlockSyncStarted() {
        //TOOD 区块同步时触发回调函数
    }
    @Override
    public void OnBlockHeightIncreased(int currentBlockHeight, double progress) {
        //TOOD 同步过程中的回调函数
    }
    @Override
    public void OnBlockSyncStopped() {
        //TOOD 同步结束触发该回调函数
    }
});

### 4.2 获取ChainID和获取子钱包余额接口
String _chainId = subWallet.GetChainId();
long balance = subWallet.GetBalance();


