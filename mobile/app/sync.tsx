import { useState } from "react";
import { View, Text, Button, Alert, StyleSheet } from "react-native";
import { api } from "../services/api";
import { appleHealthAdapter } from "../services/health/appleHealthAdapter";
import { healthConnectAdapter } from "../services/health/healthConnectAdapter";

export default function Sync() {
  const [busy,setBusy]=useState(false);
  const sync=async()=>{
    setBusy(true);
    try {
      const adapter = require("react-native").Platform.OS==="ios" ? appleHealthAdapter : healthConnectAdapter;
      if (!(await adapter.available())) throw new Error("Health provider not available on this device");
      if (!(await adapter.requestPermissions())) throw new Error("No requested health permissions granted");
      const rows=await adapter.read(new Date(Date.now()-7*86400000),new Date());
      const result=await api("/api/v1/health/sync",{method:"POST",body:JSON.stringify(rows)});
      Alert.alert("Sync complete",JSON.stringify(result));
    } catch(e:any) { Alert.alert("Sync unavailable",e.message); }
    finally { setBusy(false); }
  };
  return <View style={s.c}>
    <Text style={s.h}>Health synchronization</Text>
    <Text>Manual sync imports the last 7 days after you grant permissions.</Text>
    <Button title={busy?"Syncing...":"Sync now"} disabled={busy} onPress={sync}/>
    <Text style={s.note}>Background sync is best-effort and OS-controlled. Configure a background task only after the provider read functions are implemented and tested.</Text>
  </View>;
}
const s=StyleSheet.create({c:{flex:1,padding:24,gap:18},h:{fontSize:25,fontWeight:"700"},note:{color:"#68778A"}});
