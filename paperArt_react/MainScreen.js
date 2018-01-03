import React from 'react';
import { View, Text, Button, TextInput, Linking, AsyncStorage } from 'react-native';

const categories = [
    {id: 1, name: 'Kusudama', description: 'Flower-like spheres', imageName: 'kusudama'},
    {id: 2, name: 'Modular', description: 'Origami composed of 2 or more modules', imageName: 'modular'},
    {id: 3, name: 'Pure', description: 'Origami from one sheet of paper, no cuts', imageName: 'pure'}
];
const items = [
    {id: 1, categoryId: 1, name: 'Cat', paperType: 'Regular', color: 'Grey', duration: 50, createdAt: new Date()},
    {id: 2, categoryId: 1, name: 'Dog', paperType: 'Tant', color: 'Black', duration: 50, createdAt: new Date()},
    {id: 3, categoryId: 2, name: 'Dragon', paperType: 'Kami', color: 'Red', duration: 50, createdAt: new Date()},
    {id: 4, categoryId: 2, name: 'Fox', paperType: 'Regular', color: 'Orange', duration: 50, createdAt: new Date()},
    {id: 5, categoryId: 3, name: 'Morning Glory', paperType: 'Regular', color: 'Multicolor', duration: 50, createdAt: new Date()}
];

export default class MainScreen extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            subject: "",
            message: "",
            email: "sas_oana_7@yahoo.com",
            categories: [],
            items: []
        }
        this.loadData();
    }

    componentWillMount() {
        this.loadData();
    }

    async loadData () {
        /*AsyncStorage.removeItem('categories');
        AsyncStorage.removeItem('items');
        AsyncStorage.setItem('categories', JSON.stringify(categories));
        AsyncStorage.setItem('items', JSON.stringify(items));*/

        let response = await AsyncStorage.getItem('categories');
        let categoriesList = await JSON.parse(response) || [];
        //console.log(categoriesList);

        if(categoriesList.length !== 0)
        {
            this.setState({categories: categoriesList});
        }
        else
        {
            AsyncStorage.setItem("categories", JSON.stringify(this.state.categories));
        }

        let response2 = await AsyncStorage.getItem('items');
        let itemsList = await JSON.parse(response2) || [];

        if(itemsList.length !== 0)
        {
            this.setState({items: itemsList});
        }
        else
        {
            AsyncStorage.setItem("items", JSON.stringify(this.state.items));
        }
    }

    render() {
        const { navigate } = this.props.navigation;
        return(
            <View style={{ flex: 1, alignItems: 'center', marginTop: 30, justifyContent: 'center' }}>
                <Text>Paper Art</Text>
                <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
                    <Text>Subject:</Text>
                    <TextInput
                        style={{width: 200, borderColor: 'gray', borderWidth: 1, textAlign: 'center', margin: 10}}
                        onChangeText={(text) => this.setState({subject: text})}
                        value={this.state.subject}
                    />
                    <Text>Message:</Text>
                    <TextInput
                        style={{width: 200, borderColor: 'gray', borderWidth: 1, textAlign: 'center', margin: 10}}
                        onChangeText={(text) => this.setState({message: text})}
                        multiline={true}
                        value={this.state.message}
                    />
                    <View style={{marginBottom: 20}}>
                        <Button
                            onPress={()=> this.onPress()}
                            title="Send email"
                        />
                    </View>
                    <Button
                        onPress={() => navigate('Categories', {data: {categories: this.state.categories, items: this.state.items}})}
                        title="Paper models list"
                    />
                </View>
            </View>
        );
    }

    onPress() {
        Linking.openURL('mailto:' + this.state.email + '?&subject=' + this.state.subject + '&body=' + this.state.message);
    }
}
