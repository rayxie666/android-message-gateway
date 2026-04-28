package com.messagegateway.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.messagegateway.data.model.ForwardMethod
import com.messagegateway.data.model.MatchType
import com.messagegateway.ui.viewmodel.RuleEditorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuleEditorScreen(
    ruleId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: RuleEditorViewModel = viewModel()
) {
    LaunchedEffect(ruleId) {
        if (ruleId != null) viewModel.loadRule(ruleId)
    }

    LaunchedEffect(viewModel.isSaved) {
        if (viewModel.isSaved) onNavigateBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (viewModel.isEditing) "Edit Rule" else "New Rule") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Rule Name
            OutlinedTextField(
                value = viewModel.name,
                onValueChange = { viewModel.name = it },
                label = { Text("Rule Name") },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.nameError != null,
                supportingText = viewModel.nameError?.let { error -> { Text(error) } },
                singleLine = true
            )

            // Filter Section
            Text("Filter Conditions", style = MaterialTheme.typography.titleSmall)

            OutlinedTextField(
                value = viewModel.senderPattern,
                onValueChange = { viewModel.senderPattern = it },
                label = { Text("Sender Pattern (optional)") },
                placeholder = { Text("e.g. +1234 or regex") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Match Type
            Text("Match Type", style = MaterialTheme.typography.bodySmall)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MatchType.entries.forEach { type ->
                    FilterChip(
                        selected = viewModel.senderMatchType == type,
                        onClick = { viewModel.senderMatchType = type },
                        label = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) }
                    )
                }
            }

            OutlinedTextField(
                value = viewModel.contentKeyword,
                onValueChange = { viewModel.contentKeyword = it },
                label = { Text("Content Keyword (optional)") },
                placeholder = { Text("Match if message contains...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Divider()

            // Forward Method
            Text("Forward Method", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ForwardMethod.entries.forEach { method ->
                    FilterChip(
                        selected = viewModel.forwardMethod == method,
                        onClick = { viewModel.forwardMethod = method },
                        label = { Text(method.name) }
                    )
                }
            }

            // Target fields
            if (viewModel.forwardMethod == ForwardMethod.SMS || viewModel.forwardMethod == ForwardMethod.BOTH) {
                OutlinedTextField(
                    value = viewModel.targetPhone,
                    onValueChange = { viewModel.targetPhone = it },
                    label = { Text("Target Phone Number") },
                    placeholder = { Text("+1234567890") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            if (viewModel.forwardMethod == ForwardMethod.EMAIL || viewModel.forwardMethod == ForwardMethod.BOTH) {
                OutlinedTextField(
                    value = viewModel.targetEmail,
                    onValueChange = { viewModel.targetEmail = it },
                    label = { Text("Target Email") },
                    placeholder = { Text("example@gmail.com") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

            if (viewModel.targetError != null) {
                Text(
                    text = viewModel.targetError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Save Button
            Button(
                onClick = { viewModel.save() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
