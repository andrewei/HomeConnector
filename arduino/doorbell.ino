/*
  DigitalReadSerial
 Reads a digital input on pin 2, prints the result to the serial monitor

 This example code is in the public domain.
 */

// digital pin 2 has a pushbutton attached to it. Give it a name:
int pushButton = 2;
int led = 3;
// the setup routine runs once when you press reset:
void setup() {
  Serial.begin(9600);
  // make the pushbutton's pin an input:
  pinMode(pushButton, INPUT);
  pinMode(led, OUTPUT);
}

// the loop routine runs over and over again forever:
void loop() {
 
  // read the input pin:
  int buttonState = digitalRead(pushButton);
  if(buttonState == 1){
    // print out the state of the button:
    Serial.println("1");
    digitalWrite(3, HIGH);
    bool blinker = true;
    for(int i = 0; i < 20; i++){
      if(blinker == true){
            digitalWrite(3, HIGH);
            blinker = false;
            delay(250);
      }
      else{
         digitalWrite(3,LOW);  
         blinker = true; 
         delay(250);
     }
     digitalWrite(3,HIGH);
  }
  }
 
  digitalWrite(3, HIGH);
  delay(1);        // delay in between reads for stability   
}
