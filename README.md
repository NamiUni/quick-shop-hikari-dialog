# QuickShop-Hikari-Dialog

A [Paper](https://papermc.io/) plugin that replaces QuickShop-Hikari's default shop interactions with modern Minecraft
dialog UIs (1.21.4+).

---

## Requirements

| Dependency       | Version               |
|------------------|-----------------------|
| Paper            | **1.21.11** or later  |
| QuickShop-Hikari | **6.2.0.11** or later |

Optional dependencies:

- **MiniPlaceholders** — Enables `<audience>` and global placeholder tags inside translation messages.
- **LuckPerms** — Recommended for fine-grained permission management.

---

## Installation

1. Place plugin jar in your server's `plugins/` folder.
2. Ensure [QuickShop-Hikari](https://modrinth.com/plugin/quickshop-hikari) is installed and loaded **before** this
   plugin.
3. Start the server once to generate `plugins/QuickShop-Hikari-Dialog/config.conf` and the default translation files.
4. Configure `plugins/QuickShop-Hikari/interaction.yml` as described below, then reload or restart.

---

## QuickShop-Hikari: interaction.yml Setup

QuickShop-Hikari-Dialog registers three behavior identifiers that must be assigned in QuickShop-Hikari's
`interaction.yml`.

| Identifier            | Description                                                              |
|-----------------------|--------------------------------------------------------------------------|
| `CREATION_DIALOG`     | Opens a dialog to create a new shop by clicking a container or wall sign |
| `MODIFICATION_DIALOG` | Opens a dialog to modify an existing shop                                |
| `TRADE_DIALOG`        | Opens a dialog to buy from or sell to an existing shop                   |

### Recommended configuration

```yaml
# plugins/QuickShop-Hikari/interaction.yml

STANDING_LEFT_CLICK_SIGN: TRADE_DIALOG
STANDING_RIGHT_CLICK_SIGN: MODIFICATION_DIALOG
STANDING_LEFT_CLICK_SHOPBLOCK: TRADE_DIALOG
STANDING_RIGHT_CLICK_SHOPBLOCK: NONE # reserved for opening the chest
STANDING_LEFT_CLICK_CONTAINER: CREATION_DIALOG
STANDING_RIGHT_CLICK_CONTAINER: NONE
```

You may freely reassign these identifiers to other click actions depending on your server's preferences.

---

## Permissions

| Permission                            | Description                          | Default |
|---------------------------------------|--------------------------------------|---------|
| `qshdialog.command.admin.reload`      | Reload config and translations       | OP      |
| `qshdialog.command.shopdialog.create` | Use the `/shopdialog create` command | `true`  |
| `qshdialog.command.shopdialog.modify` | Use the `/shopdialog modify` command | `true`  |
| `qshdialog.command.shopdialog.trade`  | Use the `/shopdialog trade` command  | `true`  |

QuickShop-Hikari's own permissions continue to control which dialog inputs are shown to each player.

---

## Custom Placeholder Tags

The following tags are available inside translation messages.

### `<shop:...>`

| Tag                           | Description                                 |
|-------------------------------|---------------------------------------------|
| `<shop:name>`                 | Raw shop name (empty string if unset)       |
| `<shop:name_or_default>`      | Shop name, falling back to `<owner>'s Shop` |
| `<shop:owner_name>`           | Owner's username                            |
| `<shop:owner_display_name>`   | Owner's display name                        |
| `<shop:owner_balance>`        | Owner's current balance                     |
| `<shop:price>`                | Shop price                                  |
| `<shop:trade_type>`           | Localised trade type label                  |
| `<shop:currency>`             | Currency identifier                         |
| `<shop:stock>`                | Current stock count (in trade units)        |
| `<shop:space>`                | Available container space (in trade units)  |
| `<shop:display_visible>`      | `true` / `false`                            |
| `<shop:infinite_stock>`       | `true` / `false`                            |
| `<shop:product_display_name>` | Product's effective display name            |

### `<item:...>`

| Tag                   | Description                         |
|-----------------------|-------------------------------------|
| `<item:min_price>`    | Minimum allowed price for this item |
| `<item:max_price>`    | Maximum allowed price for this item |
| `<item:max_quantity>` | Maximum stack size of the product   |

### `<quickshop:key>` / `<quickshop:key:arg0:arg1>`

Renders a QuickShop-Hikari message by its translation key, with optional arguments.  
Each argument is parsed as MiniMessage, so other tags can be nested:

```properties
qsh_dialog.trade.error.shop_out_of_stock=<quickshop:shop-stock-too-low:'<shop:stock>':'<shop:product_display_name>'>
```

---
