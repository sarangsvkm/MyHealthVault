import { useEffect, useState } from "react";
import { View, Text, Button, ScrollView, StyleSheet } from "react-native";
import { api } from "../services/api";
import { Link } from "expo-router";

export default function Dashboard() {
  const [records,setRecords]=useState<any[]>([]);
  const [error,setError]=useState("");
  useEffect(()=>{api("/api/v1/health/records?type=WEIGHT").then(setRecords).catch(e=>setError(e.message));},[]);
  return <ScrollView contentContainerStyle={s.c}>
    <Text style={s.h}>Health dashboard</Text>
    <Text style={s.p}>Recent weight records: {records.length}</Text>
    {!!error && <Text>{error}</Text>}
    <Link href="/connections">Connect health apps</Link>
    <Link href="/reports">Medical reports</Link>
    <Link href="/sync">Sync health data</Link>
    <Button title="Refresh" onPress={()=>api("/api/v1/health/records?type=WEIGHT").then(setRecords).catch(e=>setError(e.message))}/>
  </ScrollView>;
}
const s=StyleSheet.create({c:{padding:24,gap:18},h:{fontSize:26,fontWeight:"700"},p:{fontSize:16}});
