import { useState } from "react";
import { View, Text, TextInput, Button, StyleSheet, Alert } from "react-native";
import { api, saveToken } from "../services/api";
import { router } from "expo-router";

export default function Login() {
  const [email,setEmail]=useState("");
  const [password,setPassword]=useState("");
  const [displayName,setDisplayName]=useState("");
  const [register,setRegister]=useState(false);
  const submit=async()=>{
    try {
      const result=await api(register?"/api/v1/auth/register":"/api/v1/auth/login",{
        method:"POST",body:JSON.stringify(register?{email,password,displayName}:{email,password})
      });
      await saveToken(result.accessToken); router.replace("/dashboard");
    } catch(e:any) { Alert.alert("Authentication failed",e.message); }
  };
  return <View style={s.c}>
    <Text style={s.h}>{register?"Create account":"Login"}</Text>
    <TextInput style={s.i} autoCapitalize="none" placeholder="Email" value={email} onChangeText={setEmail}/>
    {register && <TextInput style={s.i} placeholder="Display name" value={displayName} onChangeText={setDisplayName}/>}
    <TextInput style={s.i} placeholder="Password (minimum 12 characters)" secureTextEntry value={password} onChangeText={setPassword}/>
    <Button title={register?"Register":"Login"} onPress={submit}/>
    <Button title={register?"Have an account? Login":"Create account"} onPress={()=>setRegister(!register)}/>
  </View>;
}
const s=StyleSheet.create({c:{flex:1,padding:24,justifyContent:"center"},h:{fontSize:26,fontWeight:"700",marginBottom:20},i:{borderWidth:1,borderColor:"#CCD3DE",padding:13,borderRadius:9,marginBottom:12}});
