import React from 'react';
import {ListView, Text, View, Image, TouchableOpacity, AsyncStorage, Button, Modal } from "react-native";
import { Bar } from 'react-native-pathjs-charts';

export default class CategoryList extends React.Component {
    state = {
        chartVisibility: false,
    }
    constructor(props) {
        super(props);
        const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
        const { params } = this.props.navigation.state;
        this.loadData = this.loadData.bind(this);
        this.state = {
            categories: params.data.categories,
            items: params.data.items,
            itemsCount: params.data.items.length + 1,
            dataSource: ds.cloneWithRows(params.data.categories),
            chartVisibility: false,
        };
        this.loadData();
    }

    componentWillMount() {
        this.loadData();
    }

    async loadData () {
        let response = await AsyncStorage.getItem('categories');
        let categoriesList = await JSON.parse(response) || [];
        //console.log(categoriesList);

        if(categoriesList.length !== 0)
        {
            this.setState({categories: categoriesList});
        }

        let response2 = await AsyncStorage.getItem('items');
        let itemsList = await JSON.parse(response2) || [];
        //console.log(itemsList);

        if(itemsList.length !== 0)
        {
            this.setState({items: itemsList});
        }
        this.setState({itemsCount: itemsList.length + 1});
    }

    getItems(categoryId) {
        let itemsForCategory = [];
        let items = this.state.items;
        //console.log(items);
        for(let i=0; i < items.length; ++i) {
            if(items[i].categoryId === categoryId) {
                itemsForCategory = itemsForCategory.concat(items[i]);
            }
        }
        //console.log(itemsForCategory);
        return itemsForCategory;
    }

    getDataForChart() {
        let data = [];
        for(let i=0; i < this.state.categories.length; ++i) {
            let point = [];
            let items = 0;
            for (let j=0; j < this.state.items.length; ++j){
                if (this.state.items[j].categoryId === this.state.categories[i].id) {
                    items++;
                }
            }
            point.push({"nbItems": items, "name": this.state.categories[i].name});
            data.push(point);
        }
        
        return data;
    }

    renderFooter = () => {
        return (
            <View style={{paddingTop: 20, alignItems: 'center'}}>
                <Button
                    onPress={() => {this.toggleVisibility(true)}}
                    title="Show chart"
                />
            </View>
        )
    };

    toggleVisibility(visibility) {
        this.setState({chartVisibility: visibility});
    }

    render() {
        const { navigate } = this.props.navigation;
        const { params } = this.props.navigation.state;
        let options = {
            width: 300,
            height: 300,
            margin: {
                top: 20,
                left: 25,
                bottom: 50,
                right: 20
            },
            color: '#2980B9',
            gutter: 20,
            animate: {
                type: 'oneByOne',
                duration: 200,
                fillTransition: 3
            },
            axisX: {
                showAxis: true,
                showLines: true,
                showLabels: true,
                showTicks: true,
                zeroAxis: false,
                orient: 'bottom',
                label: {
                    fontFamily: 'Arial',
                    fontSize: 8,
                    fontWeight: true,
                    fill: '#34495E',
                    rotate: 45
                }
            },
            axisY: {
                showAxis: true,
                showLines: true,
                showLabels: true,
                showTicks: true,
                zeroAxis: false,
                orient: 'left',
                label: {
                    fontFamily: 'Arial',
                    fontSize: 8,
                    fontWeight: true,
                    fill: '#34495E'
                }
            }
        };

        let data = this.getDataForChart();
        return (
            <View>
                <ListView
                    renderFooter={this.renderFooter}
                    dataSource={this.state.dataSource}
                    renderRow={(rowData) => (
                        <TouchableOpacity  style={{flexDirection: 'row', paddingLeft: 40, paddingTop: 10}}
                                           onPress={() => navigate('List', {loadData: this.loadData, categoryId: rowData.id,
                                               items: this.getItems(rowData.id), itemsCount: this.state.itemsCount})}>
                            <View style={{flex: 1, alignSelf: 'center'}}>
                                <Text>Category: {rowData.name}</Text>
                                <Text>Description: {rowData.description}</Text>
                            </View>
                            <View style={{flex: 1}}>
                                <Image
                                    style={{width: 70, height: 70}}
                                    source={{uri: 'https://www.iconexperience.com/_img/g_collection_png/standard/512x512/flower.png'}}
                                />
                            </View>
                        </TouchableOpacity>
                    )}
                />
                <Modal
                    animationType = {"slide"} transparent = {false}
                    visible={this.state.chartVisibility}
                    onRequestClose = {() => { console.log("Modal has been closed.")}}>
                    <View style={{flex: 1, alignItems: 'center', paddingTop: 30}}>
                        <Text>Number of items in each category</Text>
                        <View style={{paddingLeft: 10, paddingRight: 10}}>
                            <Bar data={data} options={options} accessorKey='nbItems'/>
                        </View>
                        <Button
                            onPress={() => {this.toggleVisibility(false)}}
                            title="Close"
                        />
                    </View>
                </Modal>
            </View>
        );
    }
}