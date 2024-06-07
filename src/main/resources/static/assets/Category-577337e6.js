import{a as r}from"./api-9de8bbcf.js";import{_ as V,r as k,o as I,b as y,a as t,w as a,aO as c,e as o,X as _,f as n,g as s,d as v,t as b}from"./index-ba8ae7a7.js";import{V as x}from"./VToolbar-f66d05f2.js";import{V as h}from"./VDialog-493d3ece.js";import{V as g,a as f,d as D,c as C}from"./VCard-a98264cd.js";import{V as T,a as w,b as O}from"./VRow-c9acae30.js";import{V as m}from"./VSpacer-9429de5a.js";const j={getCategoryList(){return r.get("/categories")},getCategory(e){return r.get(`/categories/${e}`)},createCategory(e){return r.post("/categories",e)},updateCategory(e,l){return r.put(`/categories/${e}`,l)},deleteCategory(e){return r.delete(`/categories/${e}`)}};const z={name:"Category",data:()=>({dialog:!1,dialogDelete:!1,loading:!0,headers:[{title:"id",align:"center",sortable:!0,key:"id"},{title:"名称",align:"center",key:"name"},{title:"操作",align:"center",key:"actions",sortable:!1}],desserts:[],editedIndex:-1,editedItem:{name:""},defaultItem:{name:""},search:""}),computed:{formTitle(){return this.editedIndex===-1?"创建品类":"编辑品类"}},watch:{dialog(e){e||this.close()},dialogDelete(e){e||this.closeDelete()}},created(){this.initialize()},methods:{initialize(){this.fetchCategory()},fetchCategory(){this.loading=!0,j.getCategoryList().then(e=>{this.desserts=e.data,this.loading=!1})},editItem(e){this.editedIndex=this.desserts.indexOf(e),this.editedItem=Object.assign({},e),this.dialog=!0},deleteItem(e){this.editedIndex=this.desserts.indexOf(e),this.editedItem=Object.assign({},e),this.dialogDelete=!0},deleteItemConfirm(){this.desserts.splice(this.editedIndex,1),this.closeDelete()},close(){this.dialog=!1,this.$nextTick(()=>{this.editedItem=Object.assign({},this.defaultItem),this.editedIndex=-1})},closeDelete(){this.dialogDelete=!1,this.$nextTick(()=>{this.editedItem=Object.assign({},this.defaultItem),this.editedIndex=-1})},save(){this.editedIndex>-1?Object.assign(this.desserts[this.editedIndex],this.editedItem):this.desserts.push(this.editedItem),this.close()}}},B={style:{height:"100vh"}},U={class:"text-h5"};function $(e,l,N,S,L,d){const p=k("v-data-table");return I(),y("div",B,[t(p,{"fixed-header":"",height:"80vh",sticky:"true",loading:e.loading,headers:e.headers,items:e.desserts,search:e.search,"items-per-page-text":"每页显示条数","sort-by":[{key:"calories",order:"asc"}],class:"elevation-1"},{top:a(()=>[t(x,{color:"#fefefe",style:{height:"10vh"}},{default:a(()=>[t(c,{modelValue:e.search,"onUpdate:modelValue":l[0]||(l[0]=i=>e.search=i),"append-icon":"mdi-magnify",label:"搜索","single-line":"","hide-details":""},null,8,["modelValue"]),t(h,{modelValue:e.dialog,"onUpdate:modelValue":l[3]||(l[3]=i=>e.dialog=i),"max-width":"500px"},{activator:a(({props:i})=>[t(o,_({color:"primary",dark:"",class:"mb-2"},i,{onClick:l[1]||(l[1]=u=>e.dialog=!0)}),{default:a(()=>[t(n,null,{default:a(()=>[s("mdi-plus")]),_:1}),s(" 新建品类 ")]),_:2},1040)]),default:a(()=>[t(g,null,{default:a(()=>[t(f,null,{default:a(()=>[v("span",U,b(d.formTitle),1)]),_:1}),t(D,null,{default:a(()=>[t(T,null,{default:a(()=>[t(w,null,{default:a(()=>[t(O,{cols:"auto",sm:"12",md:"12"},{default:a(()=>[t(c,{modelValue:e.editedItem.name,"onUpdate:modelValue":l[2]||(l[2]=i=>e.editedItem.name=i),label:"名称"},null,8,["modelValue"])]),_:1})]),_:1})]),_:1})]),_:1}),t(C,null,{default:a(()=>[t(m),t(o,{color:"blue-darken-1",variant:"text",onClick:d.close},{default:a(()=>[s(" 取消 ")]),_:1},8,["onClick"]),t(o,{color:"blue-darken-1",variant:"text",onClick:d.save},{default:a(()=>[s(" 保存 ")]),_:1},8,["onClick"])]),_:1})]),_:1})]),_:1},8,["modelValue"]),t(h,{modelValue:e.dialogDelete,"onUpdate:modelValue":l[4]||(l[4]=i=>e.dialogDelete=i),"max-width":"500px"},{default:a(()=>[t(g,null,{default:a(()=>[t(f,{class:"text-h5"},{default:a(()=>[s("确定要删除该项吗？")]),_:1}),t(C,null,{default:a(()=>[t(m),t(o,{color:"blue-darken-1",variant:"text",onClick:d.closeDelete},{default:a(()=>[s("取消")]),_:1},8,["onClick"]),t(o,{color:"blue-darken-1",variant:"text",onClick:d.deleteItemConfirm},{default:a(()=>[s("确定")]),_:1},8,["onClick"]),t(m)]),_:1})]),_:1})]),_:1},8,["modelValue"])]),_:1})]),"item.actions":a(({item:i})=>[t(n,{size:"small",class:"me-2",onClick:u=>d.editItem(i.raw)},{default:a(()=>[s(" mdi-pencil ")]),_:2},1032,["onClick"]),t(n,{size:"small",onClick:u=>d.deleteItem(i.raw)},{default:a(()=>[s(" mdi-delete ")]),_:2},1032,["onClick"])]),"no-data":a(()=>[t(o,{color:"primary",onClick:d.initialize},{default:a(()=>[s(" 重置 ")]),_:1},8,["onClick"])]),_:1},8,["loading","headers","items","search"])])}const G=V(z,[["render",$]]);export{G as default};
