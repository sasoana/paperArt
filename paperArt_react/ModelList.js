import React from 'react';
import {ListView, Text, View, Image, TouchableOpacity, Button, AsyncStorage } from "react-native";
import Toast from 'react-native-simple-toast';

export default class ModelList extends React.Component {
    constructor(props) {
        super(props);
        this.updateItem = this.updateItem.bind(this);
        this.saveItem = this.saveItem.bind(this);
        const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
        const { params } = this.props.navigation.state;
        this.state = {
            itemsCount: params.itemsCount,
            categoryId: params.categoryId,
            items: params.items,
            dataSource: ds.cloneWithRows(params.items),
        };
    }

    async filterByCategoryId() {
        let newArray = [];
        let response = await AsyncStorage.getItem('items');
        let items = await JSON.parse(response);
        //console.log(items);
        for(var i = 0; i < items.length; i += 1) {
            if (items[i].categoryId === this.state.categoryId) {
                newArray = newArray.concat(items[i]);
            }
        }

        return newArray;
    }

    async updateItem(id, name, paper, color, duration, createdAt) {

        let response = await AsyncStorage.getItem('items');
        let newArray = await JSON.parse(response);
        //console.log(newArray);
        for(var i = 0; i < newArray.length; i += 1) {
            if (newArray[i].id === id) {
                newArray[i].name = name;
                newArray[i].paperType = paper;
                newArray[i].color = color;
                newArray[i].duration = duration;
                newArray[i].createdAt = createdAt;
            }
        }
        //console.log(newArray);
        AsyncStorage.setItem('items', JSON.stringify(newArray));
        //console.log(this.filterByCategoryId());
        this.setState({dataSource: new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2}).cloneWithRows(await this.filterByCategoryId())});
        this.setState({items: await this.filterByCategoryId()});
        this.props.navigation.state.params.loadData();

        Toast.show('The item was updated!', Toast.SHORT);
    }

    async saveItem(id, categoryId, name, paper, color, duration, createdAt) {

        let response = await AsyncStorage.getItem('items');
        let newArray = await JSON.parse(response);
        //console.log(newArray);

        newArray = newArray.concat({id: id, categoryId: categoryId, name: name,
            paperType: paper, color: color, duration: duration, createdAt: createdAt});
        AsyncStorage.setItem('items', JSON.stringify(newArray));
        console.log(newArray);
        this.setState({dataSource: new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2}).cloneWithRows(await this.filterByCategoryId())});
        this.setState({items: await this.filterByCategoryId()});
        this.props.navigation.state.params.loadData();

        let count = this.state.itemsCount;
        this.setState({itemsCount: ++count});

        Toast.show('The item was added!', Toast.SHORT);
    }

    async deleteItem(id){
        let response = await AsyncStorage.getItem('items');
        let newArray = await JSON.parse(response);

        for(let i = 0; i < newArray.length; i++)
        {
            if(newArray[i].id === id)
            {
                newArray.splice(i, 1);
            }
        }

        AsyncStorage.setItem("items", JSON.stringify(newArray));
        this.setState({dataSource: new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2}).cloneWithRows(await this.filterByCategoryId())});

        this.props.navigation.state.params.loadData();
        console.log(newArray);
        Toast.show('The item was deleted!', Toast.SHORT);
    }

    renderFooter = () => {
        const { navigate } = this.props.navigation;
        const { params } = this.props.navigation.state;
        return (
            <View style={{paddingTop: 20, alignItems: 'center'}}>
                <Button
                    onPress={() => navigate('AddItem', {saveItem: this.saveItem,
                        data: {
                            id: this.state.itemsCount,
                            name: "",
                            categoryId: params.categoryId,
                            paper: "",
                            color: "",
                            duration: 0,
                            createdAt: new Date()
                        }})}
                    title="Add new item"
                />
            </View>
        )
    }

    render() {
        const { navigate } = this.props.navigation;
        const { params } = this.props.navigation.state;
        return (
            <ListView
                dataSource={this.state.dataSource}
                renderFooter={this.renderFooter}
                renderRow={(rowData) => (
                    <TouchableOpacity  style={{flexDirection: 'row', paddingLeft: 10, paddingRight: 10,
                        paddingTop: 10, alignSelf: 'center', }}
                                       onPress={() => navigate('Details',{updateItem: this.updateItem,
                                           data: {
                                           id: rowData.id,
                                           name: rowData.name,
                                           paper: rowData.paperType,
                                           color: rowData.color,
                                           duration: rowData.duration,
                                           createdAt: rowData.createdAt
                                           }})}
                    >
                        <View style={{flex: 2, alignSelf: 'center'}}>
                            <Text>Model Name: {rowData.name}</Text>
                            <Text>Paper Type: {rowData.paperType}</Text>
                            <Text>Duration: {rowData.duration}</Text>
                        </View>
                        <View style={{flex: 1}}>
                            <Image
                                style={{width: 70, height: 70}}
                                source={{uri: 'https://facebook.github.io/react-native/docs/assets/favicon.png'}}
                            />
                        </View>
                        <View style={{flex: 1}}>
                            <Button
                                title={'Delete'}
                                onPress={() => this.deleteItem(rowData.id)}
                            />
                        </View>
                    </TouchableOpacity >
                )}
            />

        );
    }
}