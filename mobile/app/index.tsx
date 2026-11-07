import { Link } from "expo-router";
import { View, Text, StyleSheet, Pressable } from "react-native";

const Item = ({ title, href }: {title:string; href:any}) => (
  <Link href={href} asChild>
    <Pressable style={s.card}><Text style={s.cardText}>{title}  ›</Text></Pressable>
  </Link>
);

export default function Home() {
  return <View style={s.container}>
    <Text style={s.title}>MyHealthVault</Text>
    <Text style={s.subtitle}>Your personal health records, in one secure place.</Text>
    <Item title="Login / Register" href="/login" />
    <Item title="Health dashboard" href="/dashboard" />
    <Item title="Connect health apps" href="/connections" />
    <Item title="Upload medical report" href="/reports" />
  </View>;
}
const s=StyleSheet.create({
 container:{flex:1,padding:24,justifyContent:"center",backgroundColor:"#F6F8FC"},
 title:{fontSize:30,fontWeight:"800",color:"#173B67"},
 subtitle:{fontSize:15,color:"#52647A",marginVertical:12,marginBottom:24},
 card:{padding:18,backgroundColor:"white",borderRadius:14,marginBottom:12},
 cardText:{fontSize:16,fontWeight:"600",color:"#173B67"}
});
