import React from 'react';
import {Text, TextInput, Button, ScrollView} from "react-native";

export default class ModelDetails extends React.Component {
    constructor(props) {
        super(props);
        const { params } = this.props.navigation.state;
        this.state = {
            id: params.data.id,
            name: params.data.name,
            paper: params.data.paper,
            color: params.data.color,
            duration: params.data.duration
            };
    }

    render() {
        return (
            <ScrollView style={{paddingRight: 20, paddingLeft: 20}}>
                <Text>Model name:</Text>
                <TextInput
                    style={{borderColor: 'gray', borderWidth: 1, textAlign: 'center',
                        margin: 10}}
                    onChangeText={(text) => this.setState({name: text})}
                    value={this.state.name}
                    multiline={true}
                />
                <Text>Paper type:</Text>
                <TextInput
                    style={{borderColor: 'gray', borderWidth: 1, textAlign: 'center',
                        margin: 10}}
                    onChangeText={(text) => this.setState({paper: text})}
                    value={this.state.paper}
                />
                <Text>Color:</Text>
                <TextInput
                    style={{borderColor: 'gray', borderWidth: 1, textAlign: 'center', margin: 10}}
                    onChangeText={(text) => this.setState({color: text})}
                    value={this.state.color}
                />
                <Text>Duration:</Text>
                <TextInput
                    style={{borderColor: 'gray', borderWidth: 1, textAlign: 'center', margin: 10}}
                    keyboardType='numeric'
                    onChangeText={(text) => this.setState({duration: text})}
                    value={this.state.duration.toString()}
                />
                <Button
                    onPress={()=> {this.props.navigation.state.params.updateItem(this.state.id, this.state.name,
                        this.state.paper, this.state.color, this.state.duration);
                        this.props.navigation.goBack();}}
                    title="Save changes"
                />

            </ScrollView>
        )
    }
}