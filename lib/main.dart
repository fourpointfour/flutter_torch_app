import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main()
{
  runApp(MaterialApp(
    debugShowCheckedModeBanner: false,
    home: MyTorchApp(),
  ));
}

class MyTorchApp extends StatefulWidget {
  @override
  _MyTorchAppState createState() => _MyTorchAppState();
}

class _MyTorchAppState extends State<MyTorchApp> {
  bool _switchValue = false;

  static const platform = const MethodChannel('com.vaibhav.fluttertorchapp/testing');

  Future<void> _toggleFlashlight() async{
    String message = '';
    try{
      final String resultMessage = await platform.invokeMethod('toggleFlashlight');
    } on PlatformException catch (err) {
      message = 'Failed to toggle Flashlight: ${err.message}';
    }
    // you can setState() as true or false so as to indicate torch on or off
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Torch App'),),
      body: Center(
        child: Switch(
          value: _switchValue,
          onChanged: (val){
            // invoke method natively
            // toggle _switchValue
          },
        ),
      ),
    );
  }
}
