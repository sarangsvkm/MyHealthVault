import { View, Text, Button, StyleSheet, Alert } from "react-native";
import { router } from "expo-router";

export default function Connections() {
  return <View style={s.c}>
    <Text style={s.h}>Health connections</Text>
    <Text>Connect on the device where your health data is stored.</Text>
    <Button title="Connect Apple Health (iPhone)" onPress={()=>router.push("/sync?provider=APPLE_HEALTH")}/>
    <Button title="Connect Health Connect (Android)" onPress={()=>router.push("/sync?provider=HEALTH_CONNECT")}/>
    <Button title="ABDM / ABHA status" onPress={()=>Alert.alert("ABDM","Sandbox credentials and approved HIU workflow are required.")}/>
    <Text style={s.note}>HealthKit and Health Connect are native integrations. Build and test using a development build, not Expo Go.</Text>
  </View>;
}
const s=StyleSheet.create({c:{flex:1,padding:24,gap:18},h:{fontSize:25,fontWeight:"700"},note:{color:"#68778A"}});
