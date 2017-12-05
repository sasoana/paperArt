import React from 'react';
import {ListView, Text, View, Image, TouchableOpacity } from "react-native";
import PaperItem from "./PaperItem";

export default class ModelList extends React.Component {
    constructor(props) {
        super(props);
        this.items = [new PaperItem(1, "Cat", "Regular", "Grey", 20),
            new PaperItem(2, "Dog", "Tant", "Black", 34),
            new PaperItem(3, "Dragon", "Tant", "Gold", 340),
            new PaperItem(4, "Hydrangea", "Washi", "Purple", 58)];
        this.updateItem = this.updateItem.bind(this);
        const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
        this.state = {
            dataSource: ds.cloneWithRows(this.items),
        };
    }

    updateItem(id, name, paper, color, duration) {
        var newArray = this.items.slice();
        var index = -1;
        for(var i = 0; i < newArray.length; i += 1) {
            if (newArray[i].id === id) {
                newArray[i].name = name;
                newArray[i].paperType = paper;
                newArray[i].color = color;
                newArray[i].duration = duration;
            }
        }
        this.setState({dataSource: new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2}).cloneWithRows(newArray)});
    }

    render() {
        const { navigate } = this.props.navigation;
        return (
            <ListView
                dataSource={this.state.dataSource}
                renderRow={(rowData) => (
                    <TouchableOpacity  style={{flexDirection: 'row', paddingLeft: 40, paddingTop: 10, alignSelf: 'center', }}
                                       onPress={() => navigate('Details',{updateItem: this.updateItem,
                                           data: {
                                           id: rowData.id,
                                           name: rowData.name,
                                           paper: rowData.paperType,
                                           color: rowData.color,
                                           duration: rowData.duration
                                           }})}
                    >
                        <View style={{flex: 1, alignSelf: 'center'}}>
                            <Text>Model Name: {rowData.name}</Text>
                            <Text>Paper Type: {rowData.paperType}</Text>
                        </View>
                        <View style={{flex: 1}}>
                            <Image
                                style={{width: 70, height: 70}}
                                source={{uri: 'https://facebook.github.io/react-native/docs/assets/favicon.png'}}
                            />
                        </View>
                    </TouchableOpacity >
                )}
            />
        );
    }
}