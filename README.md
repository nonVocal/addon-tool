######  This addon can show all installed SAP ECTR addons and enable/disable individual addons.

If you wanna use this then
 - clone
 - build
 - add a new addon under <ECTR_INST_IDR>\addons\\<YOUR_CHOSEN_DIR_NAME>
   - *NOTE:* I don't explain what folder structure you need. You pretend to be a big boy, then solve this yourself.
 - copy the contents of the config folder into your newly create addon directory
   - you might want to change the value of the preference dev.nonvocal.addon.tool.config.edit.application
     - any text editor which can open dirctories / handle will suffice
     - comment out if you only want to open the plugin folder in explorer
 - Add the OMF fnc.edit.addons
   - *NOTE:* Be brave and ask your Admin to explain this to you.
   - *NOTE2:* Hopefully for you, your admins don't have ANY sharp or heavy stuff in arms reach.
  
After you successfully installed this addon you just simply need to call fnc.edit.addons
