import { View, Text, Button, Alert, StyleSheet } from "react-native";
import * as DocumentPicker from "expo-document-picker";
import { api } from "../services/api";

export default function Reports() {
  const upload=async()=>{
    try {
      const picked=await DocumentPicker.getDocumentAsync({
        type:["application/pdf","image/jpeg","image/png"],copyToCacheDirectory:true
      });
      if(picked.canceled)return;
      const f=picked.assets[0];
      const body=new FormData();
      body.append("file",{uri:f.uri,name:f.name,type:f.mimeType||"application/octet-stream"} as any);
      const result=await api("/api/v1/reports/upload",{method:"POST",body});
      Alert.alert("Uploaded",`Report ID: ${result.id}`);
    } catch(e:any) { Alert.alert("Upload failed",e.message); }
  };
  return <View style={s.c}>
    <Text style={s.h}>Medical reports</Text>
    <Text>Upload PDF, JPEG or PNG reports (maximum 20 MB).</Text>
    <Button title="Choose and upload report" onPress={upload}/>
  </View>;
}
const s=StyleSheet.create({c:{flex:1,padding:24,gap:18},h:{fontSize:25,fontWeight:"700"}});
