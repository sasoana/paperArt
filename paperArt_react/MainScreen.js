import React from 'react';
import { View, Text, Button, TextInput, Linking } from 'react-native';

export default class MainScreen extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            subject: "",
            message: "",
            email: "sas_oana_7@yahoo.com"}
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
                    <Button
                        onPress={()=> this.onPress()}
                        title="Send email"
                    />
                    <Button
                        onPress={() => navigate('List', {})}
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
