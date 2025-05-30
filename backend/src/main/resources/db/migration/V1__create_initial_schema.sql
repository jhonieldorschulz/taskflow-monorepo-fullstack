-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Workspaces table
CREATE TABLE workspaces (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    owner_id BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Workspace members junction table
CREATE TABLE workspace_members (
    workspace_id BIGINT REFERENCES workspaces(id),
    user_id BIGINT REFERENCES users(id),
    PRIMARY KEY (workspace_id, user_id)
);

-- Boards table
CREATE TABLE boards (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    workspace_id BIGINT NOT NULL REFERENCES workspaces(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Task lists table
CREATE TABLE task_lists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    position INTEGER NOT NULL,
    board_id BIGINT NOT NULL REFERENCES boards(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Cards table
CREATE TABLE cards (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    position INTEGER NOT NULL,
    list_id BIGINT NOT NULL REFERENCES task_lists(id),
    assignee_id BIGINT REFERENCES users(id),
    due_date TIMESTAMP,
    priority VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- Card labels table
CREATE TABLE card_labels (
    card_id BIGINT REFERENCES cards(id),
    label VARCHAR(50) NOT NULL,
    PRIMARY KEY (card_id, label)
);

-- Create indexes for better performance
CREATE INDEX idx_workspace_members_user ON workspace_members(user_id);
CREATE INDEX idx_workspace_members_workspace ON workspace_members(workspace_id);
CREATE INDEX idx_boards_workspace ON boards(workspace_id);
CREATE INDEX idx_task_lists_board ON task_lists(board_id);
CREATE INDEX idx_cards_list ON cards(list_id);
CREATE INDEX idx_cards_assignee ON cards(assignee_id);
CREATE INDEX idx_cards_due_date ON cards(due_date); 