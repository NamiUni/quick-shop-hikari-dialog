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

- **MiniPlaceholders** — Enables audience and global placeholder tags inside translation messages.

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

| Identifier           | Description                                                              |
|----------------------|--------------------------------------------------------------------------|
| `SHOP_CREATE_DIALOG` | Opens a dialog to create a new shop by clicking a container or wall sign |
| `SHOP_EDIT_DIALOG`   | Opens a dialog to modify an existing shop                                |
| `TRADE_DIALOG`       | Opens a dialog to buy from or sell to an existing shop                   |

### Recommended configuration

```yaml
# plugins/QuickShop-Hikari/interaction.yml

STANDING_LEFT_CLICK_SIGN: TRADE_DIALOG
STANDING_RIGHT_CLICK_SIGN: SHOP_EDIT_DIALOG
STANDING_LEFT_CLICK_SHOPBLOCK: TRADE_DIALOG
STANDING_RIGHT_CLICK_SHOPBLOCK: NONE # reserved for opening the chest
STANDING_LEFT_CLICK_CONTAINER: SHOP_CREATE_DIALOG
STANDING_RIGHT_CLICK_CONTAINER: NONE
```

You may freely reassign these identifiers to other click actions depending on your server's preferences.

---

## Permissions

| Permission                            | Description                          | Default |
|---------------------------------------|--------------------------------------|---------|
| `qshdialog.command.qshdialog.reload`  | Reload config and translations       | `OP`    |
| `qshdialog.command.shopdialog.create` | Use the `/shopdialog create` command | `true`  |
| `qshdialog.command.shopdialog.edit`   | Use the `/shopdialog edit` command   | `true`  |
| `qshdialog.command.tradedialog`       | Use the `/tradedialog` command       | `true`  |

QuickShop-Hikari's own permissions continue to control which dialog inputs are shown to each player.

---

## Custom Placeholder Tags

The following tags are available inside translation messages.

### Shop placeholders

Available in any message that receives a `ShopBlock` context (shop create/edit/trade dialogs).

| Tag                              | Description                                                |
|----------------------------------|------------------------------------------------------------|
| `<shop_name>`                    | Raw shop name (empty string if unset)                      |
| `<shop_name_or_default>`         | Shop name, falling back to `<owner>'s Shop`                |
| `<shop_owner_name>`              | Owner's username                                           |
| `<shop_owner_balance>`           | Owner's current balance (plain number)                     |
| `<shop_owner_balance_formatted>` | Owner's current balance, formatted by the economy provider |
| `<shop_price>`                   | Shop price (plain number)                                  |
| `<shop_price_formatted>`         | Shop price, formatted by the economy provider              |
| `<shop_trade_type>`              | Localised trade type label                                 |
| `<shop_currency>`                | Currency identifier (empty string if using default)        |
| `<shop_stock>`                   | Current stock count, in trade units                        |
| `<shop_space>`                   | Available container space, in trade units                  |
| `<shop_display_visible>`         | `true` / `false`                                           |
| `<shop_infinite_stock>`          | `true` / `false`                                           |
| `<shop_product_name>`            | Product's `item_name` component                            |
| `<shop_product_display_name>`    | Product's effective display name                           |
| `<shop_product_id>`              | Product's namespaced key (e.g. `minecraft:diamond`)        |

### User placeholders

Available in all messages that receive an audience (i.e. virtually every message).

| Tag                                     | Description                                                             |
|-----------------------------------------|-------------------------------------------------------------------------|
| `<user_balance>`                        | Player's current balance (plain number)                                 |
| `<user_balance_formatted>`              | Player's current balance, formatted by the economy provider             |
| `<user_shops_current>`                  | Number of shops the player currently owns (`-1` if limits are disabled) |
| `<user_shops_max>`                      | Player's shop limit (`-1` if limits are disabled)                       |
| `<user_cost:shop_create>`               | Cost to create a shop (plain number)                                    |
| `<user_cost:shop_create_formatted>`     | Cost to create a shop, formatted by the economy provider                |
| `<user_cost:shop_edit_name>`            | Cost to rename a shop (plain number)                                    |
| `<user_cost:shop_edit_name_formatted>`  | Cost to rename a shop, formatted by the economy provider                |
| `<user_cost:shop_edit_price>`           | Cost to change a shop's price (plain number)                            |
| `<user_cost:shop_edit_price_formatted>` | Cost to change a shop's price, formatted by the economy provider        |

### Price range placeholders

| Tag                              | Description                                          |
|----------------------------------|------------------------------------------------------|
| `<price:'<item_key>':min>`       | Minimum allowed price for the given item             |
| `<price:'<item_key>':max>`       | Maximum allowed price for the given item             |

`<item_key>` is a namespaced item key such as `minecraft:diamond`. Other tags can be nested inside the argument, for example:

```properties
qsh_dialog.shop.create.dialog.input.price=Price (Range: <price:'<shop_product_id>':min> - <price:'<shop_product_id>':max>)
```

### `<quickshop:key>` / `<quickshop:key:arg0:arg1>`

Renders a QuickShop-Hikari message by its translation key, with optional arguments.
Each argument is parsed as MiniMessage, so other tags can be nested:

```properties
qsh_dialog.trade.purchase.fail.shop_out_of_stock=<quickshop:shop-stock-too-low:'<shop_stock>':'<shop_product_display_name>'>
```
